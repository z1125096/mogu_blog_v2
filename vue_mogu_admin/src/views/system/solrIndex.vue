<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-button class= "button" type="success"  @click="handInit" icon="el-icon-refresh">初始化索引</el-button>        	      
    </div>
    <iframe :src="solrServiceUrl" width="100%" height="650px;"></iframe>
  </div>
</template>

<script>
import { initIndex } from "@/api/solrIndex";
export default {
  data() {
    return {
      solrServiceUrl:  process.env.SOLR_API
    };
  },
  beforeCreate() {

  },
  created() {
    
  },
  methods: {
    handInit: function() {
      this.$confirm("此操作将初始化全部索引, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          initIndex().then(response => {
            if(response.code == "success") {
              this.$message({
                type: "success",
                message: "初始化成功!"
              });
            }
          })

        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });      
    }
  }
};
</script>
