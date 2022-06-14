package com.ayush.proms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    private Long id;
    private String time;
    private String date;
}

class BookingMain{
    public static void main(String[] args) {
        List<Booking> bookingList=new ArrayList<>();
        Booking booking1=new Booking(1L,"2pm-4pm","2022-02-21");
        Booking booking2=new Booking(2L,"2pm-4pm","2022-02-21");
        Booking booking3=new Booking(3L,"4pm-5pm","2022-02-21");
        bookingList.add(booking1);

        for (Booking b:
             bookingList) {
            boolean isAlreadyBooked=false;
            if (b.getTime().equals(booking3.getTime())){
                if (b.getDate().equals(booking3.getDate())){
                    isAlreadyBooked=true;
                }else{
                   isAlreadyBooked=false;
                }
            }else{
               isAlreadyBooked=false;
            }
            if (!isAlreadyBooked){
                bookingList.add(booking3);
                System.out.println("Booked for :"+booking3.getTime());
            }else{
                System.out.println("Already Booked for: "+booking3.getTime());
            }
        }
    }
}
