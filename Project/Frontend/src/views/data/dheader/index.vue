<template>
  <div class="header">

    <div class="bg-header">

      <div class="t-title">

        {{ $t("data.dheader") }}

          <el-select v-model="value" placeholder="select one" class="select_brand" :popper-append-to-body="false">
            <el-option
                v-for="item in options"
                :key="item.key"
                :label="item.label"
                :value="item.key"
            >
            </el-option>
          </el-select>

      </div>

    </div>
  </div>
</template>

<script>
import repoInfo from "@/views/data/repoInfo.vue";

export default {
  name: "dheader",
  created() {
    this.value = 0
  },
  data() {
    return {
      repoInfo,
      options: [{

        repo_owner: "redis",
        repo_name: "redis-py",

        key: 0,
        label: 'redis-py'
      }, {
        repo_owner: "spring-projects",
        repo_name: "spring-boot",

        key: 1,
        label: 'spring-boot'
      }, {
        repo_owner: "itzg",
        repo_name: "docker-minecraft-server",

        key: 2,
        label: 'docker-minecraft-server'
      }, {
        repo_owner: "facebookresearch",
        repo_name: "pytorch3d",

        key: 3,
        label: 'pytorch3d'
      }],
      value: "",
      repo_owner: "",
      repo_name: ""
    }
  },
  watch: {
    value: function () {

      let repo_owner = this.options[this.value].repo_owner
      let repo_name = this.options[this.value].repo_name
      console.log(repo_owner)
      console.log(repo_name)
      repoInfo.changeName(repo_owner, repo_name)
    }
  }
};
</script>

<style lang="scss">

.header {
  width: 100%;
  height: 80px;
  padding: 0 20px;

  .bg-header {
    width: 100%;
    height: 80px;
    background: url(../../../assets/data/title.png) no-repeat;
    background-size: 100% 100%;

    .t-title {
      width: 100%;
      height: 100%;
      text-align: center;
      font-size: 2em;
      line-height: 80px;
      color: #fff;


      .select_brand{
        z-index: 1000;
        position: relative;
        // 未选中任何选项的时候 placeholder的样式 需要先选中父元素 增加权重
         input::-webkit-input-placeholder {
          color: #fff;
        }
         input::-moz-input-placeholder {
          color: #fff;
        }
         input::-ms-input-placeholder {
          color: #fff;
        }

        //修改的是el-input的样式
        //一种方法是设置最里层el-input__inner的背景色 外层的两级父元素设置为透明色
        //另一种方法是从el-select到el-input__inenr的背景色都设置为需要的颜色
         .el-select,
         .el-input,
        .el-input__inner{
           background-color:transparent;
          //background-color:#08164D ;
          color:#fff;
          border:0px;
          border-radius:0px;
          text-align: center;
           font-size: 25px;
         }

        //el-input聚焦的时候 外层的border会有一个样式
        .el-select .el-input.is-focus .el-input__inner{
          border:0px;
        }

        //修改的是el-input上下的小图标的颜色
        .el-select .el-input .el-select__caret{
          color:#fff;
        }

        //修改总体选项的样式 最外层
        .el-select-dropdown{
          //background-color:transparent;
          background-color: #053a97;
          margin: 0px;
          border:0px;
          border-radius: 0px;


        }

        //修改单个的选项的样式
        .el-select-dropdown__item{
          background-color: transparent;
          color:#fff;
          font-size: 25px;
        }

        //item选项的hover样式
        .el-select-dropdown__item.hover,
        .el-select-dropdown__item:hover{
          color: #2ad3e3;

        }

        //修改的是下拉框选项内容上方的尖角
       .el-popper .popper__arrow, .el-popper .popper__arrow::after{
          display: none;
        }
      }


    }
  }
}
</style>
