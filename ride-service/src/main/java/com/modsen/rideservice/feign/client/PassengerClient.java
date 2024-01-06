package com.modsen.rideservice.feign.client;

import com.modsen.rideservice.feign.dto.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        value = "passengers",
        url = "http://localhost:9001/api/v1/passengers"
)
public interface PassengerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    PassengerResponse getPassengerById(@PathVariable("id") Long id);
}
