package tests;

import dto.Category;
import enums.CategoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import service.CategoryService;
import service.ProductService;
import utils.PrettyLogger;
import utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CategoryTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;

    @BeforeAll
    static void BeforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
    }

    @DisplayName("Категория с заданным id")
    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService
                .getCategory(id)
                .execute();
        PrettyLogger.DEFAULT.log(response.body().toString());
        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(), equalTo(id));
    }

    @DisplayName("Категория с нулевым id")
    @Test
    void getCategoryByNullIdTest() throws IOException {
        Integer id = 0;
        try {
            Response<Category> response = categoryService
                    .getCategory(id)
                    .execute();
            assertThat(response.code(), is(404));
            PrettyLogger.DEFAULT.log(response.body().toString());
            assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
            assertThat(response.body().getId(), equalTo(id));
        } catch (NullPointerException e) {
            System.out.println("Некорректный id");
        }
    }

    @DisplayName("Категория с отрицательным id")
    @Test
    void getCategoryByNegativeIdTest() throws IOException {
        Integer id = -15;
        try {
            Response<Category> response = categoryService
                    .getCategory(id)
                    .execute();
            assertThat(response.code(), is(404));
            PrettyLogger.DEFAULT.log(response.body().toString());
            assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
            assertThat(response.body().getId(), equalTo(id));
        } catch (NullPointerException e) {
            System.out.println("Некорректный id");
        }
    }
}