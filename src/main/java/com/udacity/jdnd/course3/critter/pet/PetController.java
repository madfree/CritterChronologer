package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    public PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer owner = customerService.findById(petDTO.getOwnerId());
        Pet pet = convertPetDtoToPet(petDTO);
        pet.setOwner(owner);
        petService.savePet(pet);
        return convertPetToPetDto(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPet(petId);
        return convertPetToPetDto(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.findPetsByOwnerId(ownerId);
        List<PetDTO> petDTOList= new ArrayList<>();
        for (Pet pet: pets) {
            PetDTO petDto = convertPetToPetDto(pet);
            petDTOList.add(petDto);
        }
        return petDTOList;
    }

    private static PetDTO convertPetToPetDto(Pet pet) {
        PetDTO petDto = new PetDTO();
        BeanUtils.copyProperties(pet, petDto);
        petDto.setOwnerId(pet.getOwner().getId());
        return petDto;
    }

    private static Pet convertPetDtoToPet(PetDTO petDto) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDto, pet);
        return pet;
    }
}
