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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UpdateProductTests {
    int productId;
    static Retrofit client;
    static ProductService productService;
    Faker faker = new Faker();
    Product product;

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
        PrettyLogger.DEFAULT.log(response.body().toString());
        productId = response.body().getId();
        product.setId(productId);
    }

    @SneakyThrows
    @DisplayName("Обновление price")
    @Test
    void updateProductPriceTest() {
        product.setPrice((int) ((Math.random() + 1) * 100));
        Response<Product> response = productService.updateProduct(product).execute();
        PrettyLogger.DEFAULT.log(response.body().toString());
        assertThat("Updated", response.isSuccessful());
    }

    @SneakyThrows
    @DisplayName("Обновление title")
    @Test
    void updateProductTitleTest() {
        product.setTitle(faker.food().dish());
        Response<Product> response = productService.updateProduct(product).execute();
        PrettyLogger.DEFAULT.log(response.body().toString());
        assertThat("Updated", response.isSuccessful());
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(productId).execute();
        assertThat(response.isSuccessful(), is(true));
    }
}
