package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByRequestId(Long requestId);

    List<Item> findByOwner(Long owner, PageRequest page);

    List<Item> findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(String nameText,
                                                                                               String descriptionText,
                                                                                               PageRequest page);
}