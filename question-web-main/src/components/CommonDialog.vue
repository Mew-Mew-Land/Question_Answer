  <template>
    <!-- 点击发布按钮时显示的对话框 -->
<!--    <el-dialog> 组件来创建一个对话框-->
<!--    custom-class:为组件添加特定的样式-->
<!--    @close关闭时调用函数-->
    <el-dialog
      v-model="show"
      :title="title"
      custom-class="cust-dialog"
      :width="width"
      @close="handleClose"
    >
      <!-- 对话框内容区域，使用slot插槽允许内容的动态插入 -->
      <div class="dialog-body">
        <slot></slot>
      </div>
      <!-- 对话框底部，定义按钮 -->
      <template #footer>
        <!-- 动态生成按钮，绑定类型和点击事件 -->
        <span class="dialog-footer">
          <el-button v-for="btn in buttons" :type="btn.type" @click="btn.click">
            {{ btn.text }}</el-button
          >
        </span>
      </template>
    </el-dialog>
  </template>

  <script setup>
  import { onMounted, ref } from "vue";
  // 定义组件接收的props
  const props = defineProps({
    title: {
      type: String,
      default: "标题",
    },
    showClose: {
      type: Boolean,
      default: true,
    },
    width: {
      type: String,
      default: "400px",
    },
    buttons: {
      type: Array,
    },
  });
//要显示对话框，可以设置 show.value = true；隐藏对话框时，设置 show.value = false
  const show = ref(false);

  const showDialog = () => {
    show.value = true;
  };

  const handleClose = () => {
    show.value = false;
  };
  //显式地公开组件的内部状态和方法，使其可以在父组件或其他上下文中访问
  defineExpose({
    showDialog,
    handleClose,
  });
  </script>

  <style lang="scss">
  .cust-dialog {
    max-height: 600px;
    overflow: auto;
  }
  </style>
