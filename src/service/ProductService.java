package service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import dto.Product;

import java.util.ArrayList;

public interface ProductService {

    @GET("products")
    Call<ArrayList<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProducts(@Path("id") Integer id);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Integer id);

    @PUT("products")
    Call<Product> updateProduct(@Body Product product);
}