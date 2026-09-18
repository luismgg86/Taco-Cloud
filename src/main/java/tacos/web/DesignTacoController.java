package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder") //Indica que TacoOrder se debe mantener en sesión
public class DesignTacoController {

     //indica a Spring que antes de mostras cualquier vista en este controlador agregue los datos al modelos
    @ModelAttribute // se ejecuta antes que cualquier handle, llena el modelo de datos de ingredientes
    public void addIngredientsToModel(Model model){ 

        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for(Type type: types){
            model.addAttribute(type.toString().toLowerCase(),
            filterByType(ingredients, type));
        }

    }

    //crea el objeto tacoOrder
    //Por la anotaciónd de SessionAtributes de la clase este se guarda durante toda la sesion
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    //crea automaticamente un taco vacio
    @ModelAttribute(name="taco")
    public Taco taco(){
        return new Taco();
    }

    @PostMapping
    public String processTaco(Taco taco, //hace binding de los datos mandados en el body del post en vez de settear los datos a mano
        @ModelAttribute TacoOrder tacoOrder){ //Spring busca el atributo tacoOrder en el modelo y la sesion
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}",taco);
        return "redirect:/orders/current";
    }

    @GetMapping
    public String showDesignForm(){
        return "design";
    }

    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type){
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}