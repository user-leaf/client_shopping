package com.zhy.http.okhttp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ResponseModel {
    /**
     * message : 积分不足，生成订单失败
     * status_code : 409
     * debug : {"line":40,"file":"/home/wwwroot/laravel-5.2.31/Admin/app/CommonModel.php","class":"Symfony\\Component\\HttpKernel\\Exception\\ConflictHttpException","trace":["#0 /home/wwwroot/laravel-5.2.31/Admin/app/Order.php(66): App\\CommonModel->throwMyException('\\xE7\\xA7\\xAF\\xE5\\x88\\x86\\xE4\\xB8\\x8D\\xE8\\xB6\\xB3\\xEF\\xBC\\x8C...')","#1 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/database/Connection.php(543): App\\Order->App\\{closure}(Object(Illuminate\\Database\\MySqlConnection))","#2 [internal function]: Illuminate\\Database\\Connection->transaction(Object(Closure))","#3 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(11796): call_user_func_array(Array, Array)","#4 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(6325): Illuminate\\Database\\DatabaseManager->__call('transaction', Array)","#5 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(6325): Illuminate\\Database\\DatabaseManager->transaction(Object(Closure))","#6 /home/wwwroot/laravel-5.2.31/Admin/app/Order.php(103): Illuminate\\Support\\Facades\\Facade::__callStatic('transaction', Array)","#7 /home/wwwroot/laravel-5.2.31/Admin/app/Order.php(103): Illuminate\\Support\\Facades\\DB::transaction(Object(Closure))","#8 /home/wwwroot/laravel-5.2.31/Admin/app/Http/Controllers/Api/V1/OrderController.php(65): App\\Order->submit(Array)","#9 [internal function]: App\\Http\\Controllers\\Api\\V1\\OrderController->submit(Object(Dingo\\Api\\Http\\Request))","#10 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9425): call_user_func_array(Array, Array)","#11 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9487): Illuminate\\Routing\\Controller->callAction('submit', Array)","#12 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9467): Illuminate\\Routing\\ControllerDispatcher->call(Object(App\\Http\\Controllers\\Api\\V1\\OrderController), Object(Illuminate\\Routing\\Route), 'submit')","#13 [internal function]: Illuminate\\Routing\\ControllerDispatcher->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#14 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(52): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#15 /home/wwwroot/laravel-5.2.31/Admin/app/Http/Middleware/GetUserFromToken.php(34): Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#16 [internal function]: App\\Http\\Middleware\\GetUserFromToken->handle(Object(Dingo\\Api\\Http\\Request), Object(Closure))","#17 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9965): call_user_func_array(Array, Array)","#18 [internal function]: Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Dingo\\Api\\Http\\Request))","#19 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(32): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#20 [internal function]: Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#21 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#22 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9468): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#23 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9455): Illuminate\\Routing\\ControllerDispatcher->callWithinStack(Object(App\\Http\\Controllers\\Api\\V1\\OrderController), Object(Illuminate\\Routing\\Route), Object(Dingo\\Api\\Http\\Request), 'submit')","#24 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8525): Illuminate\\Routing\\ControllerDispatcher->dispatch(Object(Illuminate\\Routing\\Route), Object(Dingo\\Api\\Http\\Request), 'App\\\\Http\\\\Contro...', 'submit')","#25 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8512): Illuminate\\Routing\\Route->runController(Object(Dingo\\Api\\Http\\Request))","#26 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8226): Illuminate\\Routing\\Route->run(Object(Dingo\\Api\\Http\\Request))","#27 [internal function]: Illuminate\\Routing\\Router->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#28 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(52): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#29 [internal function]: Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#30 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#31 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8227): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#32 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8218): Illuminate\\Routing\\Router->runRouteWithinStack(Object(Illuminate\\Routing\\Route), Object(Dingo\\Api\\Http\\Request))","#33 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8208): Illuminate\\Routing\\Router->dispatchToRoute(Object(Dingo\\Api\\Http\\Request))","#34 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Routing/Adapter/Laravel.php(65): Illuminate\\Routing\\Router->dispatch(Object(Dingo\\Api\\Http\\Request))","#35 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Routing/Router.php(559): Dingo\\Api\\Routing\\Adapter\\Laravel->dispatch(Object(Dingo\\Api\\Http\\Request), 'v1')","#36 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Http/Middleware/Request.php(112): Dingo\\Api\\Routing\\Router->dispatch(Object(Dingo\\Api\\Http\\Request))","#37 [internal function]: Dingo\\Api\\Http\\Middleware\\Request->Dingo\\Api\\Http\\Middleware\\{closure}(Object(Dingo\\Api\\Http\\Request))","#38 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9972): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#39 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(3286): Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Dingo\\Api\\Http\\Request))","#40 [internal function]: Illuminate\\Foundation\\Http\\Middleware\\CheckForMaintenanceMode->handle(Object(Dingo\\Api\\Http\\Request), Object(Closure))","#41 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9965): call_user_func_array(Array, Array)","#42 [internal function]: Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Dingo\\Api\\Http\\Request))","#43 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#44 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Http/Middleware/Request.php(113): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#45 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Http/Middleware/Request.php(89): Dingo\\Api\\Http\\Middleware\\Request->sendRequestThroughRouter(Object(Dingo\\Api\\Http\\Request))","#46 [internal function]: Dingo\\Api\\Http\\Middleware\\Request->handle(Object(Illuminate\\Http\\Request), Object(Closure))","#47 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9965): call_user_func_array(Array, Array)","#48 [internal function]: Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Illuminate\\Http\\Request))","#49 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(32): call_user_func(Object(Closure), Object(Illuminate\\Http\\Request))","#50 [internal function]: Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Illuminate\\Http\\Request))","#51 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Illuminate\\Http\\Request))","#52 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(2366): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#53 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(2350): Illuminate\\Foundation\\Http\\Kernel->sendRequestThroughRouter(Object(Illuminate\\Http\\Request))","#54 /home/wwwroot/laravel-5.2.31/Admin/public/index.php(54): Illuminate\\Foundation\\Http\\Kernel->handle(Object(Illuminate\\Http\\Request))","#55 {main}"]}
     */

    private String message;
    private int status_code;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * line : 40
     * file : /home/wwwroot/laravel-5.2.31/Admin/app/CommonModel.php
     * class : Symfony\Component\HttpKernel\Exception\ConflictHttpException
     * trace : ["#0 /home/wwwroot/laravel-5.2.31/Admin/app/Order.php(66): App\\CommonModel->throwMyException('\\xE7\\xA7\\xAF\\xE5\\x88\\x86\\xE4\\xB8\\x8D\\xE8\\xB6\\xB3\\xEF\\xBC\\x8C...')","#1 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/database/Connection.php(543): App\\Order->App\\{closure}(Object(Illuminate\\Database\\MySqlConnection))","#2 [internal function]: Illuminate\\Database\\Connection->transaction(Object(Closure))","#3 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(11796): call_user_func_array(Array, Array)","#4 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(6325): Illuminate\\Database\\DatabaseManager->__call('transaction', Array)","#5 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(6325): Illuminate\\Database\\DatabaseManager->transaction(Object(Closure))","#6 /home/wwwroot/laravel-5.2.31/Admin/app/Order.php(103): Illuminate\\Support\\Facades\\Facade::__callStatic('transaction', Array)","#7 /home/wwwroot/laravel-5.2.31/Admin/app/Order.php(103): Illuminate\\Support\\Facades\\DB::transaction(Object(Closure))","#8 /home/wwwroot/laravel-5.2.31/Admin/app/Http/Controllers/Api/V1/OrderController.php(65): App\\Order->submit(Array)","#9 [internal function]: App\\Http\\Controllers\\Api\\V1\\OrderController->submit(Object(Dingo\\Api\\Http\\Request))","#10 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9425): call_user_func_array(Array, Array)","#11 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9487): Illuminate\\Routing\\Controller->callAction('submit', Array)","#12 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9467): Illuminate\\Routing\\ControllerDispatcher->call(Object(App\\Http\\Controllers\\Api\\V1\\OrderController), Object(Illuminate\\Routing\\Route), 'submit')","#13 [internal function]: Illuminate\\Routing\\ControllerDispatcher->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#14 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(52): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#15 /home/wwwroot/laravel-5.2.31/Admin/app/Http/Middleware/GetUserFromToken.php(34): Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#16 [internal function]: App\\Http\\Middleware\\GetUserFromToken->handle(Object(Dingo\\Api\\Http\\Request), Object(Closure))","#17 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9965): call_user_func_array(Array, Array)","#18 [internal function]: Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Dingo\\Api\\Http\\Request))","#19 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(32): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#20 [internal function]: Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#21 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#22 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9468): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#23 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9455): Illuminate\\Routing\\ControllerDispatcher->callWithinStack(Object(App\\Http\\Controllers\\Api\\V1\\OrderController), Object(Illuminate\\Routing\\Route), Object(Dingo\\Api\\Http\\Request), 'submit')","#24 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8525): Illuminate\\Routing\\ControllerDispatcher->dispatch(Object(Illuminate\\Routing\\Route), Object(Dingo\\Api\\Http\\Request), 'App\\\\Http\\\\Contro...', 'submit')","#25 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8512): Illuminate\\Routing\\Route->runController(Object(Dingo\\Api\\Http\\Request))","#26 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8226): Illuminate\\Routing\\Route->run(Object(Dingo\\Api\\Http\\Request))","#27 [internal function]: Illuminate\\Routing\\Router->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#28 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(52): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#29 [internal function]: Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Dingo\\Api\\Http\\Request))","#30 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#31 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8227): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#32 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8218): Illuminate\\Routing\\Router->runRouteWithinStack(Object(Illuminate\\Routing\\Route), Object(Dingo\\Api\\Http\\Request))","#33 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(8208): Illuminate\\Routing\\Router->dispatchToRoute(Object(Dingo\\Api\\Http\\Request))","#34 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Routing/Adapter/Laravel.php(65): Illuminate\\Routing\\Router->dispatch(Object(Dingo\\Api\\Http\\Request))","#35 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Routing/Router.php(559): Dingo\\Api\\Routing\\Adapter\\Laravel->dispatch(Object(Dingo\\Api\\Http\\Request), 'v1')","#36 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Http/Middleware/Request.php(112): Dingo\\Api\\Routing\\Router->dispatch(Object(Dingo\\Api\\Http\\Request))","#37 [internal function]: Dingo\\Api\\Http\\Middleware\\Request->Dingo\\Api\\Http\\Middleware\\{closure}(Object(Dingo\\Api\\Http\\Request))","#38 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9972): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#39 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(3286): Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Dingo\\Api\\Http\\Request))","#40 [internal function]: Illuminate\\Foundation\\Http\\Middleware\\CheckForMaintenanceMode->handle(Object(Dingo\\Api\\Http\\Request), Object(Closure))","#41 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9965): call_user_func_array(Array, Array)","#42 [internal function]: Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Dingo\\Api\\Http\\Request))","#43 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Dingo\\Api\\Http\\Request))","#44 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Http/Middleware/Request.php(113): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#45 /home/wwwroot/laravel-5.2.31/Admin/vendor/dingo/api/src/Http/Middleware/Request.php(89): Dingo\\Api\\Http\\Middleware\\Request->sendRequestThroughRouter(Object(Dingo\\Api\\Http\\Request))","#46 [internal function]: Dingo\\Api\\Http\\Middleware\\Request->handle(Object(Illuminate\\Http\\Request), Object(Closure))","#47 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9965): call_user_func_array(Array, Array)","#48 [internal function]: Illuminate\\Pipeline\\Pipeline->Illuminate\\Pipeline\\{closure}(Object(Illuminate\\Http\\Request))","#49 /home/wwwroot/laravel-5.2.31/Admin/vendor/illuminate/routing/Pipeline.php(32): call_user_func(Object(Closure), Object(Illuminate\\Http\\Request))","#50 [internal function]: Illuminate\\Routing\\Pipeline->Illuminate\\Routing\\{closure}(Object(Illuminate\\Http\\Request))","#51 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(9950): call_user_func(Object(Closure), Object(Illuminate\\Http\\Request))","#52 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(2366): Illuminate\\Pipeline\\Pipeline->then(Object(Closure))","#53 /home/wwwroot/laravel-5.2.31/Admin/bootstrap/cache/compiled.php(2350): Illuminate\\Foundation\\Http\\Kernel->sendRequestThroughRouter(Object(Illuminate\\Http\\Request))","#54 /home/wwwroot/laravel-5.2.31/Admin/public/index.php(54): Illuminate\\Foundation\\Http\\Kernel->handle(Object(Illuminate\\Http\\Request))","#55 {main}"]
     */

    private DebugBean debug;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public DebugBean getDebug() {
        return debug;
    }

    public void setDebug(DebugBean debug) {
        this.debug = debug;
    }

    public static class DebugBean {
        private int line;
        private String file;
        @SerializedName("class")
        private String classX;
        private List<String> trace;

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public List<String> getTrace() {
            return trace;
        }

        public void setTrace(List<String> trace) {
            this.trace = trace;
        }
    }
}
