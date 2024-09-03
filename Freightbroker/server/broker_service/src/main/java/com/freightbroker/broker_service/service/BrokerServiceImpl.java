package com.freightbroker.broker_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightbroker.broker_service.config.CorsConfig;
import com.freightbroker.broker_service.entity.*;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BrokerServiceImpl implements BrokerService {
    @Autowired
    CargoService cargoService;
    @Autowired
    public TruckService truckService;
    @Autowired
    public ProviderService providerService;
    @Autowired
    public CargoTruckAssignmentService cargoTruckAssignmentService;
    @Autowired
    public CustomerService customerService;

    BrokerServiceImpl(CargoService cargoService, TruckService truckService, CargoTruckAssignmentService cargoTruckAssignmentService, ProviderService providerService, CustomerService customerService) {
        this.cargoService = cargoService;
        this.truckService = truckService;
        this.providerService = providerService;
        this.cargoTruckAssignmentService = cargoTruckAssignmentService;
        this.customerService = customerService;
    }

    //**************
    class Chromosome {
        List<Integer> gene;

        public Chromosome() {
            this.gene = new ArrayList<>();
        }

        public Chromosome(int numberOfCargos, List<Long> truckIDs) {
            this.gene = new ArrayList<>();
            for (int i = 0; i < numberOfCargos; i++) {
                var index = new Random().nextInt(truckIDs.size());
                gene.add(Math.toIntExact(truckIDs.get(index)));
            }
        }

        public Chromosome(Chromosome chromosome) {
            this.gene = new ArrayList<>(chromosome.gene);
        }
    }

    @Override
    public double[] getCoordinates(String location) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?format=json&q=" + location.replace(" ", "%20");
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        JSONArray jsonArray = new JSONArray(response.toString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        double latitude = Double.parseDouble(jsonObject.getString("lat"));
        double longitude = Double.parseDouble(jsonObject.getString("lon"));

        return new double[]{latitude, longitude};
    }

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c;
    }


    public class CargoAndDeliveryPrice {
        CargoDTO cargo;
        TruckDTO truck;
        double price;

        public CargoAndDeliveryPrice(CargoDTO cargo, TruckDTO truck, double price) {
            this.cargo = cargo;
            this.truck = truck;
            this.price = price;
        }

    }

    public class TruckAndProfit {
        TruckDTO truck;
        double profit;
        CargoDTO cargo;

        public TruckAndProfit(TruckDTO truck, double profit, CargoDTO cargo) {
            this.truck = truck;
            this.profit = profit;
            this.cargo = cargo;
        }
    }

    public class ProfitAndCargo {
        private double profit;
        private CargoDTO cargo;

        public ProfitAndCargo(double profit, CargoDTO cargo) {
            this.profit = profit;
            this.cargo = cargo;
        }

        public double getProfit() {
            return profit;
        }

        public CargoDTO getCargo() {
            return cargo;
        }
    }


    @Override
    public void pairCargosTrucks(int sendMail) throws IOException {
        int retryCount = 0;
        var freeCargos = cargoService.getAllFree();
        var freeTrucks = truckService.getAllFree();
        while (freeTrucks.size() != 0 && freeCargos.size() != 0 && retryCount < 5) {
            //System.out.println("retryCount = " + retryCount);
            //System.out.println("avTrucksCount = " + truckService.availableTrucksCount());
            //System.out.println("avCargosCount = " + cargoService.availableCargoCount());
            Random random = new Random();
            List<Long> truckIDs = new ArrayList<>();
            List<CargoAndDeliveryPrice> cargoAndDeliveryPrice = new ArrayList<>();
            List<TruckAndProfit> truckAndProfits = new ArrayList<>();


            for (var truck : freeTrucks) {
                truckIDs.add(truck.getId_truck());
            }
            var chromosomes = new ArrayList<Chromosome>();
            for (int i = 0; i < 100; i++) {
                chromosomes.add(new Chromosome(cargoService.getAllFree().size(), truckIDs));
            }
            //Inceput algoritm
            for (int round = 0; round < 100; round++) {
                var parent1 = chromosomes.get(random.nextInt(0, chromosomes.size()));
                var parent2 = chromosomes.get(random.nextInt(0, chromosomes.size()));
                Chromosome parent = new Chromosome(parent1);

                //Incrucisare

                var chance = random.nextInt(0, 100);
                if (chance < 90) {
                    var parentAux = new Chromosome();
                    var threshold = random.nextInt(0, 100);
                    for (int i = 0; i < parent1.gene.size(); i++) {
                        var alpha = random.nextInt(0, 100);
                        if (alpha < threshold) {
                            parentAux.gene.add(parent1.gene.get(i));
                        } else {
                            parentAux.gene.add(parent2.gene.get(i));
                        }
                    }
                    parent = new Chromosome(parentAux);
                }
                //Mutatie
                if (chance < 5) {
                    int reset_size = parent.gene.size() / 3;
                    for (int i = 0; i < reset_size; i++) {
                        parent.gene.set(random.nextInt(0, parent.gene.size()), Math.toIntExact(truckIDs.get(random.nextInt(0, truckIDs.size()))));
                    }
                }

                //Simulare
                //System.out.println("Gene: " + parent.gene);
                int i = 0;
                for (var cargo : freeCargos) {
                    var truck = truckService.findByIdTruck((long) (parent.gene.get(i)));
                    if (truck.isPresent() && truck.get().getStatus().trim().equalsIgnoreCase("Liber")
                            && cargo.getStatus().trim().equalsIgnoreCase("Neonorată")) {
                        //Daca depaseste greutatea
                        if (cargo.getGreutate() > truck.get().getTonajMaxim() * 0.9) {
                            //Da, depaseste
                            if (round == 0)
                                cargoAndDeliveryPrice.add(new CargoAndDeliveryPrice(cargo, truck.get(), Double.POSITIVE_INFINITY));
                            //System.out.println("\n");
                            //System.out.println("Nu este fezabil: tonaj depasit");
                            //System.out.println("Gr. cargo: " + cargo.getGreutate() + " Gr.camion: " + truck.get().getTonajMaxim());

                        } else {//Daca depaseste gabaritul
                            if (cargo.getVolum() > truck.get().getVolum()) {
                                //Da, depaseste
                                if (round == 0)
                                    cargoAndDeliveryPrice.add(new CargoAndDeliveryPrice(cargo, truck.get(), Double.POSITIVE_INFINITY));
                                //System.out.println("\n");
                                //System.out.println("Nu este fezabil: gabarit depasit");
                                //System.out.println("Volum cargo: " + cargo.getVolum() + " Volum camion: " + truck.get().getVolum());

                            } else {
                                //Conditii indeplinite
                                double pretCursa = cargo.getDistanta_cursa() * truck.get().getPretKM();
                                if (round == 0)
                                    cargoAndDeliveryPrice.add(new CargoAndDeliveryPrice(cargo, truck.get(), pretCursa));
                                else {
                                    if (pretCursa < cargoAndDeliveryPrice.get(i).price) {
                                        cargoAndDeliveryPrice.set(i, new CargoAndDeliveryPrice(cargo, truck.get(), pretCursa));
                                    }
                                }
                                double distantaCamionLaPctStart = calculateDistance(truck.get().getLatitudineLocatieInitiala(), truck.get().getLongitudineLocatieInitiala(), cargo.getLatitudine_pct_ridicare(), cargo.getLongitudine_pct_ridicare());
                                //System.out.println("\n");
                                //System.out.println("Fezabil: dist. start camion - start cursa:\n" +
                                //        "de la " + truck.get().getLocatieInitiala() + " la " + cargo.getAdresa_ridicare() + " : " + distantaCamionLaPctStart);

                            }
                        }
                    }
                    i++;
                }

            }
            //Sfarsit simulare

            //System.out.println("\n\n\n\nDupa cele 3 runde:\n");
            /*
            for (var cargo : cargoAndDeliveryPrice) {

            System.out.println("ID Cargo: " + cargo.cargo.getId_cargo());
            System.out.println("ID Camion: " + cargo.truck.getId_truck());
            System.out.println("Pret: " + cargo.price);

            }*/

            //Calculare profit camion
            for (var cargo : cargoAndDeliveryPrice) {
                if (cargo.price != Double.POSITIVE_INFINITY) {
                    var truckOptional = truckService.findByIdTruck(cargo.truck.getId_truck());
                    if (truckOptional.isPresent()) {
                        var truck = truckOptional.get();
                        double distantaCamionLaPctStart = calculateDistance(truck.getLatitudineLocatieInitiala(), truck.getLongitudineLocatieInitiala(), cargo.cargo.getLatitudine_pct_ridicare(), cargo.cargo.getLongitudine_pct_ridicare());
                        double profit = truck.getPretKM() * cargo.cargo.getDistanta_cursa() -
                                truck.getCostTransport() * (cargo.cargo.getDistanta_cursa() + distantaCamionLaPctStart);

                        //System.out.println("Profit camion ID:" + truck.getId_truck() + " - " + profit);

                        truckAndProfits.add(new TruckAndProfit(truck, profit, cargo.cargo));
                    }
                }
            }

            Map<Long, ProfitAndCargo> maxProfits = new HashMap<>();
            for (var truckAndProfit : truckAndProfits) {
                if (maxProfits.containsKey(truckAndProfit.truck.getId_truck())) {
                    ProfitAndCargo current = maxProfits.get(truckAndProfit.truck.getId_truck());
                    if (truckAndProfit.profit > current.getProfit()) {
                        maxProfits.put(truckAndProfit.truck.getId_truck(), new ProfitAndCargo(truckAndProfit.profit, truckAndProfit.cargo));
                    }
                } else {
                    maxProfits.put(truckAndProfit.truck.getId_truck(), new ProfitAndCargo(truckAndProfit.profit, truckAndProfit.cargo));
                }
            }


            //Afisare profit camion
            //System.out.println("\n\n\n\nProfituri pentru camioane:\n");
            //for (var entry : maxProfits.entrySet()) {
            //System.out.println("ID Camion: " + entry.getKey() + ", Profit: " + entry.getValue());
            //}

            //Rezervare camion
            var firstTruckOptional = maxProfits.entrySet().stream().findFirst();
            if (firstTruckOptional.isPresent()) {
                Long truckId = firstTruckOptional.get().getKey();
                CargoDTO cargo = maxProfits.get(truckId).getCargo();
                truckService.reserveTruckByIdIfPossible(truckId);
                cargoService.reserveCargoByIdIfPossible(cargo.getId_cargo());
                var cost = cargo.getDistanta_cursa() * truckService.findByIdTruck(truckId).get().getPretKM();
                cargoTruckAssignmentService.save(new CargoTruckAssignmentDTO(new CargoTruckAssignmentID(cargo.getId_cargo(), truckId), firstTruckOptional.get().getValue().profit, cost));
                //notificam cele doua parti!
                if (sendMail == 1) {
                    sendNotification(customerService.findById(cargo.getId_comerciant()).get().getEmail(), "Ofertă nouă!", "Vești bune! Ați primit o ofertă nouă! Vizitați pagina noastră pentru a o vizualiza!");
                    sendNotification(providerService.findById((truckService.findByIdTruck(truckId)).get().getIdTransportator()).get().getEmail(), "Ofertă nouă!", "Vești bune! Ați primit o ofertă nouă! Vizitați pagina noastră pentru a o vizualiza!");
                }
                //System.out.println("Camion ID: " + truckId + " cu profit "+firstTruckOptional.get().getValue().profit+" ia Cargo ID: " + cargo.getId_cargo()+" cu costul "+cost );
            } else {
                retryCount++;
                System.out.println("n-am gasit nimic");
            }
            freeCargos = cargoService.getAllFree();
            freeTrucks = truckService.getAllFree();
        }
        System.out.println("Am terminat pair-ul");
    }

    @Override
    public void releaseAll() {
        truckService.releaseTrucksIfPossible();
        cargoService.releaseCargosIfPossible();
        cargoTruckAssignmentService.deleteAll();
    }

    @Override
    public Object getOfferForCustomer(long idCargo, long idCustomer) {
        var cargo = cargoService.findByIdCargo(idCargo);
        if (cargo.isPresent()) {
            if (cargo.get().getId_comerciant() != idCustomer)
                return null;
            else {
                var cargosAndTrucks = cargoTruckAssignmentService.getAllByIdCargo(idCargo);
                var cargoAndTruck = cargoTruckAssignmentService.getAllByIdCargo(idCargo).stream().findFirst().get();
                var truck = truckService.findByIdTruck(cargoAndTruck.getId().getId_truck());
                if (truck.isPresent()) {
                    var provider = providerService.findById(truck.get().getIdTransportator());
                    if (provider.isPresent())
                        //mai e de tratat cazul in care a fost deja acceptata cursa de catre transportator
                        return new ProviderAndCostDTO(provider.get(), cargoAndTruck.getCost(), cargoAndTruck.getAccepted_by_client(), cargoAndTruck.getAccepted_by_provider());
                }
            }
            return null;

        }
        return null;
    }

    @Override
    public Object acceptOfferForCustomer(long idCargo, long idCustomer) throws IOException {
        var cargo = cargoService.findByIdCargo(idCargo);
        if (cargo.isPresent()) {
            if (cargo.get().getId_comerciant() != idCustomer)
                return null;
            else {
                var offer = cargoTruckAssignmentService.getAllByIdCargo(idCargo).stream().findFirst();
                if (offer.isPresent()) {
                    if (offer.get().getAccepted_by_client() != 1) {
                        offer.get().setAccepted_by_client(1);
                        sendNotification(providerService.findById(truckService.findByIdTruck(offer.get().getId().getId_truck()).get().getIdTransportator()).get().getEmail(), "Ofertă acceptată", "Vești bune! Vă anunțăm că oferta dvs. a fost acceptată de către comerciant! 😀");
                        return "OK";
                    }
                } else return null;
            }
            return null;
        }
        return null;

    }

    public Object rejectOfferForCustomer(long idCargo, long idCustomer) throws IOException {
        var cargo = cargoService.findByIdCargo(idCargo);
        if (cargo.isPresent()) {
            if (cargo.get().getId_comerciant() != idCustomer) {
                return null;
            } else {
                var offer = cargoTruckAssignmentService.getAllByIdCargo(idCargo).stream().findFirst();
                if (offer.isPresent()) {
                    var truck = truckService.findByIdTruck(offer.get().getId().getId_truck());
                    if (truck.isPresent()) {
                        truck.get().setStatus("Liber");
                        cargoTruckAssignmentService.deleteByIdCargo(idCargo);
                        cargo.get().setStatus("Neonorată");
                        sendNotification(providerService.findById(truck.get().getIdTransportator()).get().getEmail(), "Ofertă refuztă", "Vești proaste! Vă anunțăm că oferta dvs. a fost refuzată de către comerciant! 😞");
                        return "OK";
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }


    @Override
    public Object getOfferForProvider(long idTruck, long idProvider) {
        var truck = truckService.findByIdTruck(idTruck);
        if (truck.isPresent()) {
            if (truck.get().getIdTransportator() != idProvider) {
                return null;
            } else {
                var cargosAndTrucks = cargoTruckAssignmentService.getAllByIdTruck(idTruck);
                var cargoAndTruck = cargosAndTrucks.stream().findFirst().get();
                System.out.println(cargoAndTruck.getId().getId_cargo() + " " + cargoAndTruck.getCost() + " " + cargoAndTruck.getProfit());
                var cargo = cargoService.findByIdCargo(cargoAndTruck.getId().getId_cargo());
                if (cargo.isPresent()) {
                    var customer = customerService.findById(cargo.get().getId_comerciant());
                    if (customer.isPresent())
                        return new CustomerAndProfitDTO(customer.get(), cargo.get(), cargoAndTruck.getProfit(), cargoAndTruck.getAccepted_by_client(), cargoAndTruck.getAccepted_by_provider());
                }
            }
            return null;

        }
        return null;
    }

    @Override
    public Object acceptOfferForProvider(long idTruck, long idProvider) throws IOException {
        var truck = truckService.findByIdTruck(idTruck);
        if (truck.isPresent()) {
            if (truck.get().getIdTransportator() != idProvider) {
                return null;
            } else {
                var offer = cargoTruckAssignmentService.getAllByIdTruck(idTruck).stream().findFirst();
                if (offer.isPresent()) {
                    if (offer.get().getAccepted_by_provider() != 1) {
                        offer.get().setAccepted_by_provider(1);
                        sendNotification(customerService.findById(cargoService.findByIdCargo(offer.get().getId().getId_cargo()).get().getId_comerciant()).get().getEmail(), "Ofertă acceptată", "Vești bune! Vă anunțăm că oferta dvs. a fost acceptată de către transportator! 😀");
                        return "OK";
                    }
                } else return null;
            }
            return null;

        }
        return null;
    }

    @Override
    public Object rejectOfferForProvider(long idTruck, long idProvider) throws IOException {
        var truck = truckService.findByIdTruck(idTruck);
        if (truck.isPresent()) {
            if (truck.get().getIdTransportator() != idProvider) {
                return null;
            } else {
                var offer = cargoTruckAssignmentService.getAllByIdTruck(idTruck).stream().findFirst();
                if (offer.isPresent()) {
                    var cargo = cargoService.findByIdCargo(offer.get().getId().getId_cargo());
                    if (cargo.isPresent()) {
                        cargo.get().setStatus("Neonorată");
                        cargoTruckAssignmentService.deleteByIdTruck(idTruck);
                        truck.get().setStatus("Liber");
                        sendNotification(customerService.findById(cargo.get().getId_comerciant()).get().getEmail(), "Ofertă refuztă", "Vești proaste! Vă anunțăm că oferta dvs. a fost refuzată de către transportator! 😞");
                        return "OK";
                    }
                } else {
                    return null;
                }
            }
            return null;

        }
        return null;
    }

    @Override
    public void sendNotification(String email, String subject, String content) throws IOException {
        try {
            URL url = new URL("http://" + CorsConfig.getIp() + ":8083/api/freightbroker/sendMail");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            String mailJson = objectMapper.writeValueAsString(new MailDTO(email, subject, content));
            byte[] mailBytes = mailJson.getBytes();

            var conOutputStream = con.getOutputStream();
            conOutputStream.write(mailBytes);
            con.getResponseCode();
        } catch (IOException e) {
            System.out.println("Nu s-a putut trimite email-ul");
        }
    }

    @Override
    public Object deleteCargo(long idCargo) throws IOException {
        try {
            cargoTruckAssignmentService.deleteByIdCargo(idCargo);
            cargoService.deleteByIdCargo(idCargo);
            return "OK";
        } catch (Exception e) {
            return "error";
        }
    }

    @Override
    public Object addCargo(CargoDTO cargo) throws IOException {
        try {
            try {
                cargo.setLatitudine_pct_ridicare(getCoordinates(cargo.getAdresa_ridicare())[0]);
                cargo.setLongitudine_pct_ridicare(getCoordinates(cargo.getAdresa_ridicare())[1]);
                cargo.setLatitudine_pct_livrare(getCoordinates(cargo.getAdresa_livrare())[0]);
                cargo.setLongitudine_pct_livrare(getCoordinates(cargo.getAdresa_livrare())[1]);
                cargo.setDistanta_cursa(calculateDistance(cargo.getLatitudine_pct_ridicare(), cargo.getLongitudine_pct_ridicare(), cargo.getLatitudine_pct_livrare(), cargo.getLongitudine_pct_livrare()));
            } catch (IOException e) {
                return "error";
            }
            cargoService.save(cargo);
            pairCargosTrucks(1);
            return "OK";
        } catch (Exception e) {
            return "error";
        }
    }

    @Override
    public Object addTruck(TruckDTO truck) throws IOException {
        try {
            try {
                truck.setLatitudineLocatieInitiala(getCoordinates(truck.getLocatieInitiala())[0]);
                truck.setLongitudineLocatieInitiala(getCoordinates(truck.getLocatieInitiala())[1]);
            } catch (IOException e) {
                return "error";
            }
            truckService.save(truck);
            pairCargosTrucks(1);
            return "OK";
        } catch (Exception e) {
            return "error";
        }
    }

    @Override
    public Object deleteTruck(long idTruck) throws IOException {
        try {
            cargoTruckAssignmentService.deleteByIdTruck(idTruck);
            truckService.deleteByIdTruck(idTruck);
            return "OK";
        } catch (Exception e) {
            return "error";
        }
    }


    //Testare


    public void addCargoNoPair(CargoDTO cargo) throws IOException {
        try {
            try {
                cargo.setLatitudine_pct_ridicare(getCoordinates(cargo.getAdresa_ridicare())[0]);
                cargo.setLongitudine_pct_ridicare(getCoordinates(cargo.getAdresa_ridicare())[1]);
                cargo.setLatitudine_pct_livrare(getCoordinates(cargo.getAdresa_livrare())[0]);
                cargo.setLongitudine_pct_livrare(getCoordinates(cargo.getAdresa_livrare())[1]);
                cargo.setDistanta_cursa(calculateDistance(cargo.getLatitudine_pct_ridicare(), cargo.getLongitudine_pct_ridicare(), cargo.getLatitudine_pct_livrare(), cargo.getLongitudine_pct_livrare()));
            } catch (IOException e) {
                System.out.println(e);
            }
            cargoService.save(cargo);
        } catch (Exception e) {
        }
    }


    public CargoDTO createDummyCargoDTO(String status, long idComerciant, String adresaRidicare, String adresaLivrare, int volum, int greutate) throws ParseException {
        var cargoDTO = new CargoDTO();
        cargoDTO.setStatus("Neonorată");
        cargoDTO.setId_comerciant(idComerciant);
        cargoDTO.setAdresa_ridicare(adresaRidicare);
        cargoDTO.setAdresa_livrare(adresaLivrare);
        cargoDTO.setVolum(volum);
        cargoDTO.setGreutate(greutate);
        cargoDTO.setCerinte_suplimentare(" ");
        cargoDTO.setComentarii(" ");
        cargoDTO.setData_critica_livrare(new SimpleDateFormat("yyyy-MM-dd").parse("2024-07-07"));
        return cargoDTO;
    }

    public void addTruckNoPair(TruckDTO truck) throws IOException {
        try {
            try {
                truck.setLatitudineLocatieInitiala(getCoordinates(truck.getLocatieInitiala())[0]);
                truck.setLongitudineLocatieInitiala(getCoordinates(truck.getLocatieInitiala())[1]);
            } catch (IOException e) {
                System.out.println(e);
            }
            truckService.save(truck);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public TruckDTO createDummyTruckDTO(String status, double costTransport, String locatieInitiala, int volum, int greutate, double pretKM, long idTransportator) {
        var truckDTO = new TruckDTO();
        truckDTO.setStatus("Liber");
        truckDTO.setVolum(volum);
        truckDTO.setCostTransport(costTransport);
        truckDTO.setLocatieInitiala(locatieInitiala);
        truckDTO.setPretKM(pretKM);
        truckDTO.setTonajMaxim(greutate);
        truckDTO.setIdTransportator(idTransportator);
        return truckDTO;
    }

    public void createDefaultScenario() throws IOException, ParseException {
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Piatra Neamt", "Bacau", 13, 2200));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Piatra Neamt", "Suceava", 69, 12000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Piatra Neamt", 52, 9000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Botosani", "Bacau", 2, 400));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Piatra Neamt", "Suceava", 29, 5000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Iasi", "Botosani", 52, 9000));

        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Vaslui", 140, 24000, 3.78, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.30, "Bacau", 10, 1500, 0.99, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Botosani", 20, 3500, 4.22, 22));

    }

    public void createScenario1() throws IOException, ParseException {
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Piatra Neamt", "Bacau", 13, 2200));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Piatra Neamt", "Suceava", 69, 12000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Piatra Neamt", 52, 9000));

        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Vaslui", 140, 24000, 3.78, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.30, "Bacau", 10, 1500, 0.99, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Botosani", 20, 3500, 4.22, 22));

    }

    public void createScenario2() throws IOException, ParseException {
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Piatra Neamt", "Bacau", 13, 2200));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Piatra Neamt", "Suceava", 69, 12000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Iasi", 52, 9000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Iasi", "Bacau", 18, 2500));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Bacau", "Roman, Neamt", 79, 15000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Roman, Neamt", "Piatra Neamt", 40, 7900));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Piatra Neamt", 63, 4500));


        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Vaslui", 140, 24000, 3.78, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.30, "Bacau", 10, 1500, 0.99, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Botosani", 20, 3500, 4.22, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.60, "Iasi", 140, 22000, 4.0, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.50, "Bacau", 40, 1500, 0.79, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.10, "Suceava", 60, 3500, 3.72, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.35, "Piatra Neamt", 70, 12100, 4.92, 22));

    }

    public void createScenario3() throws IOException, ParseException {
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Piatra Neamt", "Bacau", 13, 2200));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Piatra Neamt", "Suceava", 69, 12000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Iasi", 52, 9000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Iasi", "Bacau", 18, 2500));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Bacau", "Roman, Neamt", 79, 15000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Roman, Neamt", "Piatra Neamt", 40, 7900));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Piatra Neamt", 63, 4500));

        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Piatra Neamt", "Bacau", 41, 1400));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Roman, Neamt", "Suceava", 75, 12000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Roman, Neamt", 34, 6000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Iasi", "Suceava", 63, 5200));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 21, "Bacau", "Botosani", 14, 8000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Roman, Neamt", "Piatra Neamt", 45, 6000));
        addCargoNoPair(createDummyCargoDTO("Neonorată", 20, "Botosani", "Suceava", 12, 3000));


        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Vaslui", 140, 24000, 3.78, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.30, "Bacau", 10, 1500, 0.99, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.20, "Botosani", 20, 3500, 4.22, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.60, "Iasi", 140, 22000, 4.0, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.50, "Bacau", 40, 1500, 0.79, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.10, "Suceava", 60, 3500, 3.72, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.35, "Piatra Neamt", 70, 12100, 4.92, 22));

        addTruckNoPair(createDummyTruckDTO("Liber", 1.7, "Vaslui", 140, 33000, 3.78, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.34, "Bacau", 10, 1200, 0.99, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.60, "Botosani", 20, 6500, 4.22, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.70, "Iasi", 140, 34000, 3.78, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 0.40, "Bacau", 40, 5300, 0.99, 24));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.35, "Suceava", 60, 4300, 4.22, 22));
        addTruckNoPair(createDummyTruckDTO("Liber", 1.40, "Piatra Neamt", 70, 5400, 4.22, 22));

    }

    @Transactional
    public void createBigScenario() throws IOException, ParseException {
        var random = new Random();
        String[] locations = {
                "Piatra Neamt, Romania",
                "Bacau, Romania",
                "Bucuresti, Romania",
                "Cluj Napoca, Romania",
                "Timisoara, Romania",
                "Iasi, Romania",
                "Constanta, Romania",
                "Craiova, Romania",
                "Brasov, Romania",
                "Galati, Romania",
                "Ploiesti, Romania",
                "Oradea, Romania",
                "Braila, Romania",
                "Arad, Romania",
                "Pitesti, Romania",
                "Sibiu, Romania",
                "Buzau, Romania",
                "Botosani, Romania",
                "Satu Mare, Romania",
                "Ramnicu Valcea, Romania",
                "Suceava, Romania",
                "Focsani, Romania"};

        var map = new HashMap<String, Pair<Double, Double>>();
        for (var location : locations) {
            System.out.println(location);
            var coordinates = getCoordinates(location);
            map.put(location, Pair.of(coordinates[0], coordinates[1]));
        }

        var cargoList = new ArrayList<CargoDTO>();
        for (int i = 0; i < 200; i++) {
            var indexLocatieStart = random.nextInt(0, locations.length);
            int indexLocatieStop;
            do {
                indexLocatieStop = random.nextInt(0, locations.length);
            } while (indexLocatieStop == indexLocatieStart);


            if (i % 2 == 0) {
                var locationStartPair = map.get(locations[indexLocatieStart]);
                var locationStopPair = map.get(locations[indexLocatieStop]);
                CargoDTO cargo = createDummyCargoDTO("Neonorată", 20, locations[indexLocatieStart], locations[indexLocatieStop], random.nextInt(10, 70), random.nextInt(400, 15000));
                cargo.setLatitudine_pct_ridicare(locationStartPair.getLeft());
                cargo.setLongitudine_pct_ridicare(locationStartPair.getRight());
                cargo.setLatitudine_pct_livrare(locationStopPair.getLeft());
                cargo.setLongitudine_pct_livrare(locationStopPair.getRight());
                cargo.setDistanta_cursa(calculateDistance(cargo.getLatitudine_pct_ridicare(), cargo.getLongitudine_pct_ridicare(), cargo.getLatitudine_pct_livrare(), cargo.getLongitudine_pct_livrare()));
                cargoList.add(cargo);
            } else {
                var locationStartPair = map.get(locations[indexLocatieStart]);
                var locationStopPair = map.get(locations[indexLocatieStop]);
                CargoDTO cargo = createDummyCargoDTO("Neonorată", 21, locations[indexLocatieStart], locations[indexLocatieStop], random.nextInt(10, 70), random.nextInt(400, 15000));
                cargo.setLatitudine_pct_ridicare(locationStartPair.getLeft());
                cargo.setLongitudine_pct_ridicare(locationStartPair.getRight());
                cargo.setLatitudine_pct_livrare(locationStopPair.getLeft());
                cargo.setLongitudine_pct_livrare(locationStopPair.getRight());
                cargo.setDistanta_cursa(calculateDistance(cargo.getLatitudine_pct_ridicare(), cargo.getLongitudine_pct_ridicare(), cargo.getLatitudine_pct_livrare(), cargo.getLongitudine_pct_livrare()));
                cargoList.add(cargo);
            }
        }
        cargoService.saveBulk(cargoList);
        var truckList = new ArrayList<TruckDTO>();
        for (int i = 0; i < 150; i++) {
            var indexLocatieStart = random.nextInt(0, locations.length);

            double cost = (double) random.nextInt(10, 50) / 10;
            double pret = 4 * cost;
            if (i % 2 == 0) {
                try {
                    TruckDTO truck = createDummyTruckDTO("Liber", cost, locations[indexLocatieStart], random.nextInt(15, 150), random.nextInt(750, 20000), pret, 22);
                    var locationPair = map.get(locations[indexLocatieStart]);
                    truck.setLatitudineLocatieInitiala(locationPair.getLeft());
                    truck.setLongitudineLocatieInitiala(locationPair.getRight());
                    truckList.add(truck);
                } catch (Exception ignored) {
                }
            } else {
                try {
                    TruckDTO truck = createDummyTruckDTO("Liber", cost, locations[indexLocatieStart], random.nextInt(15, 150), random.nextInt(750, 20000), pret, 24);
                    var locationPair = map.get(locations[indexLocatieStart]);
                    truck.setLatitudineLocatieInitiala(locationPair.getLeft());
                    truck.setLongitudineLocatieInitiala(locationPair.getRight());
                    truckList.add(truck);
                } catch (Exception ignored) {
                }
            }
        }
        truckService.saveBulk(truckList);

    }
}
