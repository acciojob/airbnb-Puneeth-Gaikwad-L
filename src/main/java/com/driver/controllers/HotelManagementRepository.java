package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository

public class HotelManagementRepository {
    private HashMap<String,Hotel> hotelDb = new HashMap<>();
    private HashSet<String> UniqueHotel = new HashSet<>();
    private HashMap<Integer, User> userDB = new HashMap<>();

    private HashMap<String,Booking> bookingHashMap = new HashMap<>();
    private HashMap<Integer, Integer> NoOfBooking = new HashMap<>();

    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName() == null || hotel == null || UniqueHotel.contains(hotel.getHotelName()))
            return "FAILURE";
        else {
            UniqueHotel.add(hotel.getHotelName());
            hotelDb.put(hotel.getHotelName(),hotel);
            return "SUCCESS";
        }
    }

    public Integer addUser(User user) {
        userDB.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int mostFas = 0;
        String name = "";
        for(String a : hotelDb.keySet()){
            Hotel curr = hotelDb.get(a);
            List<Facility> facilities=  curr.getFacilities();
            if(facilities.size()>mostFas){
                mostFas = facilities.size();
                name = curr.getHotelName();
            }
            if (facilities.size() == mostFas){
                ArrayList<String> list = new ArrayList<>();
                list.add(name);
                list.add(curr.getHotelName());
                Collections.sort(list);
                name = list.get(0);
            }
        }
        return name;
    }


    public int bookARoom(String uuid, Booking booking) {
        bookingHashMap.put(uuid,booking);
        int noOfrooms = booking.getNoOfRooms();
        String name  = booking.getHotelName();
        Hotel BookingHotel = hotelDb.get(name);
        if(noOfrooms> BookingHotel.getAvailableRooms()){
            return -1;
        }
        BookingHotel.setAvailableRooms(BookingHotel.getAvailableRooms()-noOfrooms);
        int totalCost = BookingHotel.getPricePerNight() * noOfrooms;
        NoOfBooking.put(booking.getBookingAadharCard(),NoOfBooking.getOrDefault(booking.getBookingAadharCard(),0)+1);
        return totalCost;
    }

    public int getBookings(Integer aadharCard) {
        return NoOfBooking.getOrDefault(aadharCard,0);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelDb.get(hotelName);
        List<Facility> list = hotel.getFacilities();
        for(Facility f : newFacilities){
            if(!list.contains(f)){
                list.add(f);
            }
        }
        hotel.setFacilities(list);
        return hotel;
    }
}
