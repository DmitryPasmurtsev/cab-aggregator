package com.modsen.rideservice.feign.client;

import com.modsen.rideservice.feign.dto.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        value = "drivers",
        url = "http://localhost:9002/api/v1/drivers"
)
public interface DriverClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    DriverResponse getDriverById(@PathVariable("id") Long id);
}
