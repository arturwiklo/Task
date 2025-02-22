package com.betacom.recruitmentTask.controller;

import com.betacom.recruitmentTask.dto.ItemRequest;
import com.betacom.recruitmentTask.model.Item;
import com.betacom.recruitmentTask.model.Users;
import com.betacom.recruitmentTask.repository.ItemRepository;
import com.betacom.recruitmentTask.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemController(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createItem(@RequestBody ItemRequest request, Authentication authentication) {

        String username = authentication.getName();
        Optional<Users> user = userRepository.findByLogin(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Item item = new Item();
        item.setName(request.getName());
        item.setOwner(user.get());
        itemRepository.save(item);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems(Authentication authentication) {
        String username = authentication.getName();
        Optional<Users> user = userRepository.findByLogin(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Item> items = itemRepository.findByOwner(user.get());
        return ResponseEntity.ok(items);
    }
}

