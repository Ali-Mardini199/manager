package com.appointments.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.appointments.manager.model.Service;
import com.appointments.manager.model.User;
import com.appointments.manager.repository.ServiceRepository;
import com.appointments.manager.repository.UserRepository;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private UserRepository userRepository;

    public Service create(Service service) {
        Long userId = service.getProvider().getId();
        User user = userRepository.findById(userId).orElse(null);
        service.setProvider(user);
        return serviceRepository.save(service);
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public Service findById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public Service update(Long id, Service updated) {
        Service service = findById(id);
        service.setName(updated.getName());
        service.setPrice(updated.getPrice());
        service.setPeriod(updated.getPeriod());
        service.setProvider(updated.getProvider());
        return serviceRepository.save(service);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }
}