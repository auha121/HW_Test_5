package tests;

import com.github.javafaker.Faker;
import dto.Product;
import enums.CategoryType;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import service.ProductService;
import utils.PrettyLogger;
import utils.RetrofitUtils;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetProductTests {
    static Retrofit client;
    static ProductService productService;
    Faker faker = new Faker();
    Product product;
    int productId;

    @BeforeAll
    static void BeforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        product.setId(productId);
    }

    @SneakyThrows
    @DisplayName("Показать все продукты")
    @Test
    void getProductTest(){
        Response<ArrayList<Product>> response = productService.getProducts().execute();
        PrettyLogger.DEFAULT.log(response.body().toString());
    }

    @SneakyThrows
    @DisplayName("Показать продукт с определенным id")
    @Test
    void getIdProductTest(){
        Response<Product> response = productService.getProducts(productId).execute();
        PrettyLogger.DEFAULT.log(response.body().toString());
    }

    @SneakyThrows
    @DisplayName("Показать продукт с нулевым id")
    @Test
    void getIdProductIdNullTest(){
        productId = 0;
        try {
            Response<Product> response = productService.getProducts(productId).execute();
            assertThat(response.code(), is(404));
            PrettyLogger.DEFAULT.log(response.body().toString());
        } catch (NullPointerException e) {
            System.out.println("Некорректный id");
        }
    }

    @SneakyThrows
    @DisplayName("Показать продукт с отрицательным id")
    @Test
    void getIdProductNegativeIdTest(){
        productId = -10;
        try {
            Response<Product> response = productService.getProducts(productId).execute();
            assertThat(response.code(), is(404));
            PrettyLogger.DEFAULT.log(response.body().toString());
        } catch (NullPointerException e) {
            System.out.println("Некорректный id");
        }
    }

    @SneakyThrows
   @AfterEach
    void tearDown() {
        try {
            Response<ResponseBody> response = productService.deleteProduct(productId).execute();
            //assertThat(response.isSuccessful(), is(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
