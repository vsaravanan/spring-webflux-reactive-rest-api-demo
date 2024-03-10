package dev.saravan.reactiveapi.service;

import dev.saravan.reactiveapi.client.UserWebClient;
import dev.saravan.reactiveapi.exception.NotFoundException;
import dev.saravan.reactiveapi.model.Address;
import dev.saravan.reactiveapi.model.User;
import dev.saravan.reactiveapi.repository.AddressRepository;
import dev.saravan.reactiveapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserWebClient userWebClient;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private  Mono<User> returnWithAddress(User userIn) {
        return Mono.just(userIn).flatMap(user -> {
            if (user.getAddressId() == null) {
                return Mono.just(user);
            }
            return addressRepository.findById(user.getAddressId()).map(address -> {
                user.setAddress(address);
                return user;
            });
        });
    }
    
    public Mono<User> getUserById(int id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .flatMap(this::returnWithAddress);

    }

    
    public Flux<User> getUsers() {
        return userRepository.findAll().flatMap(this::returnWithAddress);
    }

    
    public Mono<User> saveUser(User user) {
        if (user.getAddress() == null) return userRepository.save(user);
        return saveUserWithAddress(user);
    }

    private Mono<User> saveUserWithAddress(User user) {
        return addressRepository.save(user.getAddress()).flatMap(address -> {
            user.setAddressId(address.getId());
            return userRepository.save(user);
        });
    }



    public Mono<User> updateUser(int id, User user) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .flatMap(m -> {
                    user.setId(m.getId()); // if there is something else to update do it here.
                    if (user.getAddress() == null) return userRepository.save(user);
                    return updateUserWithAddress(user);
                });
    }

    private Mono<? extends User> updateUserWithAddress(User user) {
        return addressRepository.findById(user.getAddressId()).flatMap(address -> {
            address.setId(user.getAddressId());
            address.setCity(user.getAddress().getCity());
            address.setStreet(user.getAddress().getStreet());
            address.setState(user.getAddress().getState());
            return addressRepository.save(address).flatMap(addr -> {
                user.setAddressId(addr.getId());
                user.setAddress(addr);
                return userRepository.save(user);
            });
        });
    }

    
    public Mono<Void> deleteUser(int id) {
        getUserById(id);
        // delete user with address
        return userRepository.findById(id).flatMap(user -> {
            if (user.getAddressId() == null) return userRepository.deleteById(id);
            return  userRepository.deleteById(id).then(addressRepository.deleteById(user.getAddressId()));
        });
    }

    public Mono<Void> deleteAllUsers() {
        return userRepository.findAll()
                .flatMap(user -> {
                    Long addressId = user.getAddressId();
                    Mono<Void> deleteUserMono = userRepository.deleteById(user.getId());

                    if (addressId != null) {
                        Mono<Void> deleteAddressMono = addressRepository.deleteById(addressId);
                        return Mono.when(deleteUserMono, deleteAddressMono);
                    } else {
                        return deleteUserMono;
                    }
                })
                .then();
    }



    
    public Mono<User> getGuestUserById(int id) {
        return userWebClient.retrieveGuestUser(id);
    }


    public Flux<User> init() {
        // save a couple of users
        var users = Flux.just(
                new User("saravandev", ThreadLocalRandom.current().nextInt(1, 100)
                        , new Address("istanbul")),
                new User("saravan", ThreadLocalRandom.current().nextInt(1, 100), new Address("chennai")),
                new User("santhosh", ThreadLocalRandom.current().nextInt(1, 100), new Address("singapore"))
        );

        users.flatMap(this::saveUser)
                .subscribe(user -> log.info("Saved user: " + user))
        ;

        return getUsers();

    }

}
