package com.freightbroker.broker_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightbroker.broker_service.entity.CargoDTO;
import com.freightbroker.broker_service.entity.TruckDTO;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface BrokerService {
    public void pairCargosTrucks(int sendMail) throws IOException;

    public double[] getCoordinates(String location) throws IOException;

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    public Object getOfferForCustomer(long idCargo, long idCustomer);
    public Object acceptOfferForCustomer(long idCargo, long idCustomer) throws IOException;
    public Object rejectOfferForCustomer(long idCargo, long idCustomer) throws IOException;


    public Object getOfferForProvider(long idTruck, long idProvider);

    public Object acceptOfferForProvider(long idTruck, long idProvider) throws IOException;

    public Object rejectOfferForProvider(long idTruck, long idProvider) throws IOException;

    public void sendNotification(String email,String subject, String content) throws IOException;
    public Object deleteCargo(long idCargo) throws IOException;
    public Object addCargo(CargoDTO cargo) throws IOException;

    public Object addTruck(TruckDTO truckDTO) throws IOException;

    public Object deleteTruck(long idTruck) throws IOException;

    //Pentru debug
    public void releaseAll();

}
