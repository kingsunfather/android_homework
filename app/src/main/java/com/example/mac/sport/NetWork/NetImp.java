//package com.example.mac.sport.NetWork;
//
//import com.example.mac.sport.entity.User;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class NetImp {
//    private static String baseUrl = "http://127.0.0.1:8000/";
//
//    public static boolean register(User user){
//        //创建Retrofit对象
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        NetInterface netInterface=retrofit.create(NetInterface.class);
//        Call<User> call=netInterface.register(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                System.out.println(response.body());
//            }
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                System.out.print("-----------------------------------------------------------------");
//                System.out.println(t.getMessage());
//            }
//        });
//        return true;
//    }
//
//}
