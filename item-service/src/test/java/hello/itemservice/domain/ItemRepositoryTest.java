package hello.itemservice.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.assertj.core.api.Assertions.*;


class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    //test가 끝날 떄마다
    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }
    @Test
    void save(){
        //given
        Item item = new Item("itemA",10000,10);
        //when
        Item save = itemRepository.save(item);
        //then
        Item byId = itemRepository.findById(save.getId());

        assertThat(byId).isEqualTo(save);
    }

    @Test
    void findAll(){
        //given
        Item item1 = new Item("itemA",10000,10);
        Item item2 = new Item("itemB",20000,20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> itemList = itemRepository.findAll();
        //then
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList).contains(item1,item2);

    }

    @Test
    void updateItem(){
        //given
        Item item1 = new Item("itemA",10000,10);

        Item savedItem = itemRepository.save(item1);
        Long id = savedItem.getId();
        //when
        Item updateItem = new Item("item2", 20000, 20);
        itemRepository.update(id,updateItem);
        //then

        Item byId = itemRepository.findById(id);
        assertThat(byId.getItemName()).isEqualTo(updateItem.getItemName());
        assertThat(byId.getPrice()).isEqualTo(updateItem.getPrice());
        assertThat(byId.getQuantity()).isEqualTo(updateItem.getQuantity());
    }

    @Test
    void testKey(){
        Map<String,String> maps = new HashMap<>();
        maps.put("xonmin","dev");
        maps.put("s","desdv");
        maps.put("sdsd","dddddd");
        maps.put("adasdad","dddddsa");

        Map<String,String> maps2 = new HashMap<>();
        maps2.put("xonmin","dev");
        maps2.put("s","desdv");
        maps2.put("sdsd","dddddd");
        maps2.put("adasdad","dddddsa");

        Set<String> comp  = maps2.keySet();

        Set<String> keySet1 = maps.keySet();
        Set<String> keySet2 = maps.keySet();

        System.out.println(comp.hashCode());
        System.out.println(keySet1.hashCode());
        Assertions.assertThat(keySet1.hashCode() ==  keySet2.hashCode());
        Assertions.assertThat(comp == keySet2);

    }
}