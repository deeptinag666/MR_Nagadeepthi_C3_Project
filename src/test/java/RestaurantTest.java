import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestaurantTest {
    Restaurant restaurant;

    @BeforeEach
    public void CreateRestaurantObject(){
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);
        restaurant.addToMenu("Vegetable Cheese Sandwich", 140);
        restaurant.addToMenu("Chocolate Ice-Cream", 175);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        Restaurant mockRestaurant = Mockito.spy(restaurant);
        LocalTime currentTime = LocalTime.parse("11:30:00");
        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(currentTime);
        Boolean openStatus = mockRestaurant.isRestaurantOpen();
        assertTrue(openStatus);
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        Restaurant mockRestaurant = Mockito.spy(restaurant);
        LocalTime currentTime = LocalTime.parse("22:30:00");
        Mockito.when(mockRestaurant.getCurrentTime()).thenReturn(currentTime);
        Boolean openStatus = mockRestaurant.isRestaurantOpen();
        assertFalse(openStatus);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }

    @Test
    public void get_order_cost_greater_than_zero_for_selected_items(){
        ArrayList<String> itemsSelected = new ArrayList<>();
        itemsSelected.add("Chocolate Ice-Cream");
        itemsSelected.add("Vegetable Cheese Sandwich");
        int expectedOrderCost = 0;
        List<Item> menuItems = restaurant.getMenu();
        for(String selectedItemName:itemsSelected){
            Item item = menuItems.stream().filter(x -> x.getName().equals(selectedItemName)).findFirst().orElse(null);
            expectedOrderCost += item.getPrice();
        }
        int totalOrderCost = restaurant.getOrderValue(itemsSelected);
        assertEquals(expectedOrderCost,totalOrderCost);
    }

    @Test
    public void get_order_cost_should_display_zero_when_no_items_selected(){
        ArrayList<String> itemsSelected = new ArrayList<>();
        int totalOrderCost = restaurant.getOrderValue(itemsSelected);
        assertEquals(0,totalOrderCost);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}