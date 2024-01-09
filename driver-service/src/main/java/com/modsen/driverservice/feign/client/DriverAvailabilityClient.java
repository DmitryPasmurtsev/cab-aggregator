package com.modsen.driverservice.feign.client;

import com.modsen.driverservice.feign.dto.DriverAvailabilityCheckDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        value = "rides",
        url = "http://localhost:9003/api/v1/rides"
)
public interface DriverAvailabilityClient {

    @RequestMapping(method = RequestMethod.GET, value = "/driver/{id}/checkAvailability", produces = "application/json")
    DriverAvailabilityCheckDto checkAvailability(@PathVariable("id") Long id);
}
