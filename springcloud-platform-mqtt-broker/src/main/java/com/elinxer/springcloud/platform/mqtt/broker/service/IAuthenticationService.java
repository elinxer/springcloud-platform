
package com.elinxer.springcloud.platform.mqtt.broker.service;


import com.elinxer.springcloud.platform.mqtt.broker.entity.Authentication;
import com.elinxer.springcloud.platform.mqtt.broker.entity.ClientAuth;

import java.util.function.Consumer;

/**
 * 客户端认证服务
 */
public interface IAuthenticationService {

    /**
     * 异步认证，以 Okhttp 为例:
     * <pre>
     *     OkHttpClient client = new OkHttpClient();
     *
     *     Request request = new Request.Builder()
     *             .url("https://localhost/authenticate")
     *             .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), JSON.toJSONString(authDTO)))
     *             .build();
     *
     *     client.newCall(request).enqueue(new Callback() {
     *
     *         public void onFailure(Call call, IOException e) {
     *             onFailure.accept(e);
     *         }
     *
     *         public void onResponse(Call call, Response response) throws IOException {
     *             Authentication auth = JSON.parseObject(response.body().string(), Authentication.class);
     *             onResponse.accept(auth);
     *         }
     *     });
     * </pre>
     *
     * @param authDTO    {@link ClientAuth} 客户端认证对象
     * @param onResponse 响应成功后执行
     * @param onFailure  请求失败后响应
     */
    void asyncAuthenticate(ClientAuth authDTO, Consumer<Authentication> onResponse, Consumer<Throwable> onFailure);
}

