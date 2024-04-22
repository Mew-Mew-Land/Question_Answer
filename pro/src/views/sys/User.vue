<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input v-model="searchForm.username" placeholder="用户名" clearable />
      </el-form-item>

      <el-form-item>
        <el-button @click="getUserList">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true" v-if="hasAuth('sys:user:save')">新增</el-button>
      </el-form-item>

      <el-form-item>
        <el-popconfirm title="确定要批量删除吗？" @confirm="delHandle(null)">
          <template #reference>
            <el-button type="danger" :disabled="delBtnStatus" v-if="hasAuth('sys:user:delete')">批量删除</el-button>
          </template>
        </el-popconfirm>
      </el-form-item>
    </el-form>

    <!-- 表格区域 -->
    <el-table :data="tableData" style="width: 100%" border stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="头像" width="50">
        <template #default="{ row }">
          <el-avatar size="small" :src="row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="code" label="角色名称">
        <template #default="{ row }">
          <el-tag size="small" type="info" v-for="item in row.sysRoles" :key="item.name">{{ item.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="statu" label="状态">
        <template #default="{ row }">
          <el-tag size="small" :type="row.statu === 1 ? 'success' : 'danger'">{{ row.statu === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="created" width="200" label="创建时间"></el-table-column>
      <el-table-column prop="icon" width="260px" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="roleHandle(row.id)">分配角色</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" @click="repassHandle(row.id, row.username)">重置密码</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" @click="editHandle(row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-popconfirm title="确定要删除这条记录吗？" @confirm="delHandle(row.id)">
            <template #reference>
              <el-button type="text">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <!-- 新增对话框 -->
    <el-dialog
        title="提示"
        v-model:visible="dialogVisible"
        width="600px"
        @update:visible="handleDialogClose"
    >
    <el-form :model="editForm" :rules="editFormRules" ref="editForm">
      <el-form-item label="用户名" prop="username" label-width="100px">
        <el-input v-model="editForm.username" autocomplete="off"></el-input>
        <el-alert
            title="初始密码为888888"
            :closable="false"
            type="info"
            style="line-height: 12px;"
        ></el-alert>
      </el-form-item>

      <el-form-item label="邮箱" prop="email" label-width="100px">
        <el-input v-model="editForm.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone" label-width="100px">
        <el-input v-model="editForm.phone" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="状态" prop="statu" label-width="100px">
        <el-radio-group v-model="editForm.statu">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="resetForm('editForm')">取消</el-button>
        <el-button type="primary" @click="submitForm('editForm')">确定</el-button>
      </div>
    </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog
        title="分配角色"
        v-model:visible="roleDialogFormVisible"
        width="600px"
    >
      <el-form :model="roleForm">
        <el-tree
            :data="roleTreeData"
            show-checkbox
            ref="roleTree"
            :check-strictly="checkStrictly"
            node-key="id"
            :default-expand-all="true"
            :props="defaultProps"
        ></el-tree>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="roleDialogFormVisible=false">取消</el-button>
          <el-button type="primary" @click="submitRoleHandle('roleForm')">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';

const searchForm = reactive({});
const delBtlStatu = ref(true);
const total = ref(0);
const size = ref(10);
const current = ref(1);
const dialogVisible = ref(false);
const editForm = reactive({});
const tableData = ref([]);
const multipleSelection = ref([]);
const roleDialogFormVisible = ref(false);
const defaultProps = {
  children: 'children',
  label: 'name'
};
const roleForm = reactive({});
const roleTreeData = ref([]);
const treeCheckedKeys = ref([]);
const checkStrictly = ref(true);

const editFormRules = {
  username: [
    { required: true, message: '请输入用户名称', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' }
  ],
  statu: [
    { required: true, message: '请选择状态', trigger: 'blur' }
  ]
};

// Methods
const toggleSelection = (rows) => {
  if (rows) {
    rows.forEach(row => {
      this.$refs.multipleTable.toggleRowSelection(row);
    });
  } else {
    this.$refs.multipleTable.clearSelection();
  }
};

const handleSelectionChange = (val) => {
  console.log("勾选");
  console.log(val);
  multipleSelection.value = val;
  delBtlStatu.value = val.length === 0;
};
// Function to handle size change for pagination
const handleSizeChange = (val) => {
  console.log(`每页 ${val} 条`);
  size.value = val;
  getUserList();
};

// Function to handle current page change
const handleCurrentChange = (val) => {
  console.log(`当前页: ${val}`);
  current.value = val;
  getUserList();
};

// Function to reset the specified form
const resetForm = (formName) => {

  dialogVisible.value = false;
  Object.assign(editForm, {});
};

// Function to handle dialog close action
const handleClose = () => {
  resetForm('editForm');
};

// Function to fetch user list
const getUserList = () => {
  axios.get("/sys/user/list", {
    params: {
      username: searchForm.username,
      current: current.value,
      size: size.value,
    }
  }).then(res => {
    tableData.value = res.data.data.records;
    size.value = res.data.data.size;
    current.value = res.data.data.current;
    total.value = res.data.data.total;
  });
};

async function submitForm(formName) {
  // 省略具体验证逻辑
  const valid = true;
  if (valid) {
    try {
      const response = await axios.post(`/sys/user/${editForm.value.id ? 'update' : 'save'}`, editForm.value);
      message.success({
        showClose: true,
        message: '恭喜你，操作成功',
        onClose: getUserList,
      });
      dialogVisible.value = false;
    } catch (error) {
      console.error('提交表单失败', error);
    }
  } else {
    console.log('error submit!!');
  }
}

// editHandle 方法的 Vue 3 版本
async function editHandle(id) {
  try {
    const response = await axios.get(`/sys/user/info/${id}`);
    editForm.value = response.data.data;
    dialogVisible.value = true;
  } catch (error) {
    console.error('获取编辑表单失败', error);
  }
}

// delHandle 方法的 Vue 3 版本
async function delHandle(id) {
  let ids = [];
  if (id) {
    ids.push(id);
  } else {
    ids = multipleSelection.value.map(row => row.id);
  }
  try {
    const response = await axios.post("/sys/user/delete", ids);
    message.success({
      showClose: true,
      message: '恭喜你，操作成功',
      onClose: getUserList,
    });
  } catch (error) {
    console.error('删除操作失败', error);
  }
}

// roleHandle 方法的 Vue 3 版本
async function roleHandle(id) {
  roleDialogFormVisible.value = true;
  try {
    const response = await axios.get(`/sys/user/info/${id}`);
    roleForm.value = response.data.data;
    let roleIds = response.data.data.sysRoles.map(row => row.id);
    // 确保 roleTree 是正确的 ref 到你的树形组件
    roleTree.value.setCheckedKeys(roleIds);
  } catch (error) {
    console.error('获取角色数据失败', error);
  }
}

// submitRoleHandle 方法的 Vue 3 版本
async function submitRoleHandle() {
  let roleIds = roleTree.value.getCheckedKeys();
  try {
    const response = await axios.post(`/sys/user/role/${roleForm.value.id}`, roleIds);
    message.success({
      showClose: true,
      message: '恭喜你，操作成功',
      onClose: getUserList,
    });
    roleDialogFormVisible.value = false;
  } catch (error) {
    console.error('提交角色失败', error);
  }
}

// repassHandle 方法的 Vue 3 版本
async function repassHandle(id, username) {
  messageBox.confirm(`将重置用户【${username}】的密码, 是否继续?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      const response = await axios.post("/sys/user/repass", { id });
      message.success('恭喜你，操作成功');
    } catch (error) {
      console.error('重置密码失败', error);
    }
  }).catch(() => {
    // 取消操作处理
  });
}

</script>

<style scoped>

	.el-pagination {
		float: right;
		margin-top: 22px;
	}

</style>