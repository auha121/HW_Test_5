package tests;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import dto.Product;
import enums.CategoryType;
import service.ProductService;
import utils.PrettyLogger;
import utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostProductTests {
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

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @SneakyThrows
    @DisplayName("Создание продукта")
    @Test
    void postProductTest() {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        product.setId(productId);
        PrettyLogger.DEFAULT.log(response.body().toString());
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @SneakyThrows
    @DisplayName("Создание продукта c отрицательной ценой")
    @Test
    void postProductNegativePriceTest() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(-100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        product.setId(productId);
        PrettyLogger.DEFAULT.log(response.body().toString());
    }

    @SneakyThrows
    @DisplayName("Создание продукта без цены")
    @Test
    void postProductNoPriceTest() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        product.setId(productId);
        PrettyLogger.DEFAULT.log(response.body().toString());
    }

    @SneakyThrows
    @DisplayName("Создание продукта без заголовка title")
    @Test
    void postProductNoTypeTest() {
        product = new Product()
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        product.setId(productId);
        PrettyLogger.DEFAULT.log(response.body().toString());
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(productId).execute();
        assertThat(response.isSuccessful(), is(true));
    }
}