import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.util.ReflectionTestUtils;

class RestaurantTest {
    Restaurant restaurant;
    @Mock
    private Restaurant mockRestaurant;

    @BeforeEach
    public void setUp(){
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        mockRestaurant = Mockito.spy(restaurant);
        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("10:30:00"));

        assertTrue(mockRestaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        mockRestaurant = Mockito.spy(restaurant);
        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(LocalTime.parse("22:31:00"));

        assertFalse(mockRestaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>CUSTOMER: ADD ITEMS TO ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    //Test method addItemsToOrder
    //when adding items to order
    //should return the cost of order
    //and the cost should equal the total price of items have been added to order
    public void add_items_to_order_order_cost_should_be_total_of_items_price(){
        mockRestaurant = Mockito.spy(restaurant);
        List<Item> mockMenu = new ArrayList<>();
        mockMenu.add(new Item("Sweet corn soup", 119));
        mockMenu.add(new Item("Vegetable lasagne", 269));
        mockMenu.add(new Item("Sizzling brownie", 319));
        ReflectionTestUtils.setField(mockRestaurant, "menu", mockMenu);

        //add items to order and calculate order cost
        int orderCost = mockRestaurant.addItemsToOrder(Arrays.asList("Sweet corn soup", "Vegetable lasagne"));
        assertEquals(388, orderCost);
    }

    @Test
    //Test method addItemsToOrder
    //when adding null to order
    //should return the cost of order
    //and the cost should not change
    public void add_items_to_order_order_cost_should_not_change_when_item_list_is_null(){
        //cost of order before adding items
        int orderCostBeforeAdding = restaurant.getOrderCost();
        //cost of order after adding null
        int orderCostAfterAdding = restaurant.addItemsToOrder(null);

        assertEquals(orderCostBeforeAdding, orderCostAfterAdding);
    }

    @Test
    //Test method removeItemsFromOrder
    //when remove items from order
    //should return the cost of order
    //and the cost should decrease amount equal items price
    public void remove_items_from_order_order_cost_should_decrease_amount_of_items_price(){
        mockRestaurant = Mockito.spy(restaurant);
        //cost of order before removing items
        int orderCostBeforeRemoving = mockRestaurant.getOrderCost();

        List<Item> mockMenu = new ArrayList<>();
        mockMenu.add(new Item("Sweet corn soup", 119));
        mockMenu.add(new Item("Vegetable lasagne", 269));
        mockMenu.add(new Item("Sizzling brownie", 319));
        ReflectionTestUtils.setField(mockRestaurant, "menu", mockMenu);

        //remove items from order and calculate order cost
        int orderCostAfterRemoving = mockRestaurant.removeItemsFromOrder(Arrays.asList("Sweet corn soup", "Sizzling brownie"));
        //now order cost should decrease amount = 119 + 319 = 438
        assertEquals(438,orderCostBeforeRemoving-orderCostAfterRemoving);
    }

    @Test
    //Test method removeItemsFromOrder
    //when remove null from order
    //should return the cost of order
    //and the cost should not change
    public void remove_items_from_order_order_cost_should_not_change_when_item_list_is_null(){
        //cost of order before removing items
        int orderCostBeforeRemoving = restaurant.getOrderCost();
        //cost of order after removing null
        int orderCostAfterRemoving = restaurant.removeItemsFromOrder(null);

        assertEquals(orderCostBeforeRemoving, orderCostAfterRemoving);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<CUSTOMER: ADD ITEMS TO ORDER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}