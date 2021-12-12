package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        //Schedule newSchedule = scheduleService.createSchedule(convertScheduleDtoToSchedule(scheduleDTO));
        //return convertScheduleToScheduleDto(newSchedule);

        Schedule newSchedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, newSchedule);
        List <Long> employeeIds = scheduleDTO.getEmployeeIds();
        List <Long> petIds = scheduleDTO.getPetIds();
        List <Employee> employeeList = new ArrayList<>();
        List <Pet> petList = new ArrayList<>();
        if (employeeIds != null) {
            for (Long id: employeeIds) {
                employeeList.add(employeeService.findEmployeeById(id));
            }
        }
        if (petIds != null) {
            for (Long id: petIds) {
                petList.add(petService.findPetById(id));
            }
        }
        newSchedule.setEmployees(employeeList);
        newSchedule.setPets(petList);
        newSchedule.setActivities(scheduleDTO.getActivities());
        scheduleService.createSchedule(newSchedule);
        scheduleDTO.setId(newSchedule.getId());
        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        //List<Schedule> schedules = scheduleService.findAllSchedules();
        //return getScheduleDTOS(schedules);

        List<Schedule> tmp = scheduleService.findAllSchedules();
        List<ScheduleDTO> ret = new ArrayList<>();
        ScheduleDTO newSchedule;
        if(tmp!=null) {
            for (Schedule schedule : tmp) {
                newSchedule = new ScheduleDTO();
                BeanUtils.copyProperties(schedule, newSchedule);
                newSchedule.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
                List<Long> pets = new ArrayList<>();
                for(Pet p : schedule.getPets()){
                    System.out.println("PET ID: " + p.getId());
                    pets.add(p.getId());
                }
                newSchedule.setPetIds(pets);
                ret.add(newSchedule);
            }
        }
        return ret;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId);
        List<Schedule> schedules = scheduleService.findSchedulesByPet(pet);
        return getScheduleDTOS(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        //Employee employee = employeeService.findEmployeeById(employeeId);
        List<Schedule> schedules = scheduleService.findScheduleForEmployee(employeeId);
        return getScheduleDTOS(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.findById(customerId);
        List<Pet> pets = customer.getPets();

        List<Schedule> schedules = new ArrayList<>();
        for (Pet pet: pets) {
            schedules.addAll(scheduleService.findSchedulesByPet(pet));
        }
        return getScheduleDTOS(schedules);
    }

    private Schedule convertScheduleDtoToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        //BeanUtils.copyProperties(scheduleDTO, schedule);

        schedule.setId(scheduleDTO.getId());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(schedule.getActivities());

        List<Employee> employees = new ArrayList<>();
        if (scheduleDTO.getEmployeeIds() != null) {
            for (long employeeId: scheduleDTO.getEmployeeIds()) {
                employees.add(employeeService.findEmployeeById(employeeId));
            }
        }
        schedule.setEmployees(employees);

        List<Pet> pets = new ArrayList<>();
        if (scheduleDTO.getPetIds() != null) {
            for (long petId: scheduleDTO.getPetIds()) {
                pets.add(petService.findPetById(petId));
            }
        }
        schedule.setPets(pets);

        return schedule;
    }

    private ScheduleDTO convertScheduleToScheduleDto(Schedule schedule) {
        ScheduleDTO scheduleDto = new ScheduleDTO();
        //BeanUtils.copyProperties(schedule, scheduleDto);

        scheduleDto.setId(schedule.getId());
        scheduleDto.setDate(schedule.getDate());
        scheduleDto.setActivities(schedule.getActivities());

        List<Long> employeeIds = new ArrayList<>();
        if (schedule.getEmployees() != null) {
            for (Employee employee: schedule.getEmployees()) {
                employeeIds.add(employee.getId());
            }
        }
        scheduleDto.setEmployeeIds(employeeIds);

        List<Long> petIds = new ArrayList<>();
        if (schedule.getPets() != null) {
            for (Pet pet: schedule.getPets()) {
                petIds.add(pet.getId());
            }
        }
        scheduleDto.setPetIds(petIds);

        return scheduleDto;
    }

    private List<ScheduleDTO> getScheduleDTOS(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOS.add(convertScheduleToScheduleDto(schedule));
        }
        return scheduleDTOS;
    }
}
