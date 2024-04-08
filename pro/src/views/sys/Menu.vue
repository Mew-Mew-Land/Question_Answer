<template>
  <div>
<!--    inline表单元素以行内形式显示-->
    <el-form :inline="true">
<!--      表单项-->
      <el-form-item>
<!--        新增按钮,-->
        <el-button type="primary" @click="dialogVisible = true">新增</el-button>
      </el-form-item>
    </el-form>
<!--绑定数据-->
<!--宽度75%,外边框-->
<!--    主键-->
<!--    边框-->
<!--    交替颜色-->
<!--    默认展开所有行-->
<!--    树形数据-->
    <el-table
        :data="tableData"
        style="width: 75%; margin-bottom: 20px;"
        row-key="id"
        border
        stripe

        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
<!--      prop对应数据来源的某个-->
<!--      sortable允许这一列进行排列-->
      <el-table-column prop="name" label="名称" sortable width="180" />
      <el-table-column prop="perms" label="权限编码" sortable width="180" />
      <el-table-column prop="icon" label="图标" />

      <el-table-column prop="type" label="类型">
<!--        定义了一个插槽，其中 row 是当前行的数据对象。-->
<!--        v-slot="{ row }" 定义了一个可以接收当前行数据的模板。-->
        <template v-slot="{ row }">
          <!-- 根据 row.type 的值显示不同的标签 -->
<!--          type 设置为 "success" 时，标签会显示为绿色-->
          <el-tag size="small" v-if="row.type === 0">目录</el-tag>
          <el-tag size="small" v-else-if="row.type === 1" type="success">菜单</el-tag>
          <el-tag size="small" v-else-if="row.type === 2" type="info">按钮</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="path" label="菜单URL" />
      <el-table-column prop="component" label="菜单组件" />
      <el-table-column prop="orderNum" label="排序号" />

      <el-table-column prop="statu" label="状态">
        <template v-slot="{ row }">
          <el-tag size="small" v-if="row.statu === 1" type="success">正常</el-tag>
          <el-tag size="small" v-else-if="row.statu === 0" type="danger">禁用</el-tag>
        </template>
      </el-table-column>
<!--操作中是编辑按钮-->
      <el-table-column label="操作">
        <template v-slot="{ row }">
          <el-button type="text" @click="editHandle(row.id)">编辑</el-button>
<!--          分割线-->
          <el-divider direction="vertical" />
          <!-- 删除确认弹窗 -->
          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="delHandle(row.id)">
            <!-- 弹窗触发按钮 -->
            <template #reference>
              <el-button type="text">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 对话框使用 v-model:visible 双向绑定 -->
<!--    dialogVisible 为 true 时，对话框显示-->
<!--    @update是一个事件监听器，当对话框的可见性发生变化时，会调用 handleClose 方法。-->
    <el-dialog title="提示" v-model:visible="dialogVisible"
               width="600px" @update:visible="handleClose">
      <el-form :model="editForm" :rules="editFormRules"
               ref="editForm" label-width="100px">


        <!-- 表单内容 -->
        <el-form-item label="上级菜单" prop="parentId">
          <el-select v-model="editForm.parentId" placeholder="请选择上级菜单">

            <el-option
                v-for="item in tableData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>

            <el-option
                v-for="(child, index) in item.children"
                :key="`child-${child.id}-${index}`"
                :label="`- ${child.name}`"
                :value="child.id">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="editForm.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="权限编码" prop="perms">
          <el-input v-model="editForm.perms" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="图标" prop="icon">
          <el-input v-model="editForm.icon" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="菜单URL" prop="path">
          <el-input v-model="editForm.path" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="菜单组件" prop="component">
          <el-input v-model="editForm.component" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="editForm.type">
            <el-radio :label="0">目录</el-radio>
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="状态" prop="statu">
          <el-radio-group v-model="editForm.statu">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="排序号" prop="orderNum">
          <el-input-number v-model="editForm.orderNum" :min="1"></el-input-number>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')">立即创建</el-button>
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>>
</template>

<script>
export default {
  name: "Menu",
  data() {
    return {
      dialogVisible: false,
      editForm: {},
      editFormRules: {
        parentId: [
          { required: true, message: '请选择上级菜单', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        perms: [
          { required: true, message: '请输入权限编码', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择状态', trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: '请填入排序号', trigger: 'blur' }
        ],
        statu: [
          { required: true, message: '请选择状态', trigger: 'blur' }
        ]
      },
      tableData: []
    }
  },

  created() {
    this.getMenuTree();
  },
  methods: {
    getMenuTree() {
      this.$axios.get("/sys/menu/list").then(res => {
        this.tableData = res.data.data;
      });
    },
    submitForm(formName) {
      //this.$refs[formName] 引用了具有 ref 属性的表单实例。
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/sys/menu/' + (this.editForm.id ? 'update' : 'save'), this.editForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: '恭喜你，操作成功',
                  type: 'success',
                  onClose: () => {
                    this.getMenuTree(); // 刷新菜单树
                  }
                });

                this.dialogVisible = false; // 关闭对话框
              });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },


    editHandle(id) {

      this.$axios.get('/sys/menu/info/' + id).then(res => {
        this.editForm = res.data.data; // 更新表单数据以反映所选菜单项的信息

        this.dialogVisible = true; // 打开对话框以编辑菜单项
      });
    },
			resetForm(formName) {
				this.$refs[formName].resetFields();
				this.dialogVisible = false
				this.editForm = {}
			},
			handleClose() {
				this.resetForm('editForm')
			},
			delHandle(id) {
				this.$axios.post("/sys/menu/delete/" + id).then(res => {
					this.$message({
						showClose: true,
						message: '恭喜你，操作成功',
						type: 'success',
						onClose:() => {
							this.getMenuTree()
						}
					});

				})
			}
		}
	}
</script>

<style scoped>

</style>