package com.freightbroker.gateway_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightbroker.gateway_service.entity.CargoDTO;
import com.freightbroker.gateway_service.entity.TruckDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Base64;

@Service
public class GatewayService {

    public String getIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    public int getIdCustomer(String token){
        Claims claims = validateToken(token);
        if (claims != null) {
            return (int) claims.get("id_customer");
        } else {
            return -1;
        }
    }

    public int addNewCargoRequest(CargoDTO cargo) throws IOException {
        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/addnewcargo");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String cargoJson = objectMapper.writeValueAsString(cargo);
        byte[] cargoBytes = cargoJson.getBytes();

        var conOutputStream = con.getOutputStream();
        conOutputStream.write(cargoBytes);
        return con.getResponseCode();
    }

    public String getMyCargoRequest(long idCustomer) throws IOException {
        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/getmycargo?id_customer=" + idCustomer);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else{
            return "Error";
        }
    }


    public int addNewTruckRequest(TruckDTO truck) throws IOException {
        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/addnewtruck");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String truckJson = objectMapper.writeValueAsString(truck);
        byte[] truckBytes = truckJson.getBytes();

        var conOutputStream = con.getOutputStream();
        conOutputStream.write(truckBytes);
        return con.getResponseCode();
    }

    public String getMyTruckRequest(long idProvider) throws IOException {
        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/getmytruck?id_provider=" + idProvider);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else{
            return "Error";
        }
    }

    public String getOfferForCustomerRequest(int idCustomer, long id_cargo) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/getofferforcustomer?id_customer="+idCustomer+"&id_cargo=" + id_cargo);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else{
            return null;
        }
    }

    public String acceptOfferForCustomerRequest(int idCustomer, long id_cargo) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/acceptofferforcustomer?id_customer="+idCustomer+"&id_cargo=" + id_cargo);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            return "OK";
        }else{
            return null;
        }
    }

    public String rejectOfferForCustomerRequest(int idCustomer, long id_cargo) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/rejectofferforcustomer?id_customer="+idCustomer+"&id_cargo=" + id_cargo);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            return "OK";
        }else{
            return null;
        }
    }


    public String getOfferForProviderRequest(int id_provider, long id_truck) throws IOException {
    	URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/getofferforprovider?id_provider="+id_provider+"&id_truck=" + id_truck);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else{
            return null;
        }
    }

    public String acceptOfferForProviderRequest(int id_provider, long id_truck) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/acceptofferforprovider?id_provider="+id_provider+"&id_truck=" + id_truck);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            return "OK";
        }else{
            return null;
        }
    }

    public String rejectOfferForProviderRequest(int id_provider, long id_truck) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/rejectofferforprovider?id_provider="+id_provider+"&id_truck=" + id_truck);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            return "OK";
        }else{
            return null;
        }
    }

    public String getCustomer(int id_customer) throws IOException {
        URL url = new URL("http://"+getIp()+":8080/api/freightbroker/users?id="+id_customer+"&type=customer");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else{
            return null;
        }
    }

    public String getProvider(int id_provider) throws IOException {
        URL url = new URL("http://"+getIp()+":8080/api/freightbroker/users?id="+id_provider+"&type=provider");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }else{
            return null;
        }
    }

    public String deleteCargo(long id_cargo) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/deletecargo?id_cargo="+id_cargo);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        if(con.getResponseCode() == 200) {
            return "OK";
        }else{
            return null;
        }
    }

    public String deleteTruck(long id_truck) throws IOException {

        URL url = new URL("http://"+getIp()+":8082/api/freightbroker/brokerservice/deletetruck?id_truck="+id_truck);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        if(con.getResponseCode() == 200) {
            return "OK";
        }else{
            return null;
        }
    }
}