package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public List<Pet> findPetsByOwnerId(Long ownerId) {
        return petRepository.findAllByOwnerId(ownerId);
    }

    public Pet savePet(Pet pet) {
        Pet newPet = petRepository.save(pet);
        Customer customer = newPet.getOwner();
        if (customer != null){
            if (customer.getPets() == null) {
                List<Pet> pets = new ArrayList<>();
                pets.add(newPet);
                customer.setPets(pets);
            } else {
                customer.getPets().add(newPet);
            }
            customerRepository.save(customer);
        }
        return newPet;
    }

    public Pet getPet(long petId) {
        return petRepository.findById(petId).get();

    }

    public Pet findPetById(long petId) {
        return petRepository.findById(petId).get();
    }
}
