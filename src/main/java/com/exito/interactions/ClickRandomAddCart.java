package com.exito.interactions;

import com.exito.utils.EscrituraExcel;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static com.exito.ui.ProductsCatalogUI.*;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isPresent;

public class ClickRandomAddCart implements Interaction {

    private WebElementFacade element;
    private WebElementFacade name;
    Random random = new Random();
    int numberOfProductsToSelect = 5;

    @Override
    public <T extends Actor> void performAs(T actor) {
        WaitUntil.the(JavascriptExecutor.class.toString(), isPresent()).forNoMoreThan(60).seconds();
        List<WebElementFacade> listProducts = BTN_ADD_CART.resolveAllFor(actor);
        List<WebElementFacade> listNames = LBL_PRODUCT_NAME.resolveAllFor(actor);
        Random random = new Random();
        int numberOfProductsToSelect = 5;
        if (listProducts.size() < numberOfProductsToSelect) {
            throw new RuntimeException("No hay suficientes productos disponibles para seleccionar.");
        }

        for (int i = 0; i < numberOfProductsToSelect; i++) {
            int indexRandom = random.nextInt(listProducts.size());

            element = listProducts.get(indexRandom);
            name = listNames.get(indexRandom);

            WebDriverWait wait = new WebDriverWait(Serenity.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            Scroll.to(element);

            clickAtCenter(element);
            EscrituraExcel.escrituraExcel(
                    "src/main/resources/Data/Data.xlsx",
                    name.getText(), i + 1, 1);

            // Remove selected product from lists to prevent selecting it again
            listProducts.remove(indexRandom);
            listNames.remove(indexRandom);
        }

    }

    public static Performable click(){
        return instrumented(ClickRandomAddCart.class);
    }

    // Método para hacer clic en el centro del elemento usando JavascriptExecutor
    private void clickAtCenter(WebElementFacade element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) Serenity.getDriver();
        javascriptExecutor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        javascriptExecutor.executeScript("arguments[0].click();", element);
    }
}

