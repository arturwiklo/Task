package com.betacom.recruitmentTask.controller;

import com.betacom.recruitmentTask.dto.ItemRequest;
import com.betacom.recruitmentTask.model.Item;
import com.betacom.recruitmentTask.model.Users;
import com.betacom.recruitmentTask.repository.ItemRepository;
import com.betacom.recruitmentTask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ItemController itemController;

    private Users user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(UUID.randomUUID());
        user.setLogin("testUser");

        itemRequest = new ItemRequest();
        itemRequest.setName("Test Item");
    }

    @Test
    void shouldReturn401IfUserNotAuthenticated() {
        when(authentication.getName()).thenReturn(null);

        ResponseEntity<Void> response = itemController.createItem(itemRequest, authentication);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldCreateItemSuccessfully() {
        when(authentication.getName()).thenReturn(user.getLogin());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        ResponseEntity<Void> response = itemController.createItem(itemRequest, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void shouldReturnUserItemsSuccessfully() {
        when(authentication.getName()).thenReturn(user.getLogin());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwner(user)).thenReturn(List.of(new Item(UUID.randomUUID(), user, "Item 1")));

        ResponseEntity<List<Item>> response = itemController.getItems(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }
}

