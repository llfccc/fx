<template>
    <div>
        状态:
        <i :class="ws_status"></i>    
        <el-button type="success" @click="ws_connect">建立websocket通道</el-button>    
        <el-input v-model="text" placeholder="发送消息"></el-input>
        <p>返回内容：{{content}}</p>
        <el-button type="success" @click="ws_send">发送消息</el-button>
    </div>
</template>
<script>
export default {
    data() {
        return {
            text: '',
            content: '',
            ws_status: 'el-icon-circle-close',
            message_socket: '',
        }
    },
    created() {
        // 组件创建完后获取数据，这里和1.0不一样，改成了这个样子
        // this.message_socket = this.ws_connect()
        // this.get_projects()
        // this.get_event_types()
        // this.get_sale_event_types()
    },
    methods: {
        handleEdit: function (index, row) {
            this.editFormVisible = true;
            this.editForm = Object.assign({}, row);
        },
        ws_connect: function () {
            var self = this;
            if ("WebSocket" in window) {
                var ws_scheme = window.location.protocol == "https:" ? "wss" : "ws";
                var message_socket = new WebSocket(ws_scheme + '://localhost:8000' + "/message" + window.location.pathname);
                // return message_socket
                // var message = {
                //     handle: 'llf',
                //     message: 'hello_web',
                // }
                message_socket.onopen = function (evt) {
                    //已经建立连接
                    // message_socket.send(JSON.stringify(message));

                    self.ws_status = "el-icon-circle-check"
                };
                message_socket.onclose = function (evt) {
                    self.ws_status = "el-icon-circle-close"
                    console.log("I'm sorry. Bye!");

                };
                message_socket.onmessage = function (evt) {
                    self.content = JSON.parse(evt.data);
                    self.$notify.info({
                        title: '消息',
                        message: self.content.message,
                        offset: 100,
                    });      
                    console.log()
                // handle messages here
            };
            message_socket.onerror = function (evt) {
                console.log("ERR: " + evt.data);

            };
        } else {
            // 浏览器不支持 WebSocket
            console.log("浏览器不支持 WebSocket");
        }
            return message_socket
    },
    ws_send: function () {
        var self = this;
        // console.log(this.message_socket)
        var message_socket = this.message_socket
        message_socket.send(JSON.stringify({ "sdf": this.text }));
    }
},
}
</script>