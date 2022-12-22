<template>
  <div class="messege-box">
    <div class="messege-content" >
      <ul :style="{ 'margin-top': marginTop + '%' }"    @mouseenter="Stop()" @mouseleave="Up()">
        <li v-for="(item, index) in developerInfo" :key="index">
          <a>
            <icon :type="'tongzhi1'" :size="'1rem'" :color="'#F0FF00'"></icon>
            {{ ("000" + (item.id+1)).slice(-3)}}.Author:{{ item.author }}<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Commit Times:{{item.cnt}}
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import repoInfo from "@/views/data/repoInfo.vue";

export default {

  data() {
    return {   repoInfo,
      repo_owner:"redis",
      repo_name:"redis-py",animate:false,
      developerInfo: [


      ],
      activeIndex: 0,
      intNum: undefined,
      // num:0
    };
  },
  created() {
    this.getDeveloperInfo()
    this.getChange()
  },
  mounted(){
    // this.autoSroll("scroll_in2");
  },
  methods:{  getChange(){
      setInterval(()=>{

        if(repoInfo.shareName!==this.repo_name){
          this.repo_name=repoInfo.shareName
          this.repo_owner=repoInfo.shareOwner
          this.getDeveloperInfo2()
        }

      },5)

    },
    ScrollUp() {
      console.log("ScrollUp")
    let _this=this
      _this.intNum = setInterval(() => {
        _this.animate=true;// 向上滚动的时候需要添加css3过渡动画
        setTimeout(()=>{
          _this.developerInfo.push(_this.developerInfo[0]);// 将数组的第一个元素添加到数组的

          _this.developerInfo.shift(); //删除数组的第一个元素
          _this.animate=false;


        },1000)
      }, 2000);
    },
    //鼠标移上去停止
    Stop() {
      clearInterval(this.intNum);
      // console.log("stop")
    },
    Up() {
      this.ScrollUp();
      // console.log("start")
    },

    getDeveloperInfo() {
      // console.log("getNumRelease")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/mostActiveTop', {
            params: {top:"100"}
          })
          .then(function (response) {
            for (let i = 0; i < response.data.length ; i++) {
              _this.developerInfo.push({
                id:i,
                author:response.data[i].author,
                cnt:response.data[i].cnt
              })
            }
            // _this.developerInfo = response.data
            _this.ScrollUp();
            // console.log(response);
          })
          .catch(function (error) {
            // console.log(error);
          });
    }
    , getDeveloperInfo2() {
      // console.log("getNumRelease")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/mostActiveTop', {
            params: {top:"100"}
          })
          .then(function (response) {
            _this.developerInfo=[]
            for (let i = 0; i < response.data.length ; i++) {
              _this.developerInfo.push({
                id:i,
                author:response.data[i].author,
                cnt:response.data[i].cnt
              })
            }
            // _this.developerInfo = response.data

            // console.log(response);
          })
          .catch(function (error) {
            // console.log(error);
          });
    }
  },


};
</script>

<style lang="scss">
.messege-box {
  width: 100%;
  height: 100%;
  padding: 15px 15px 25px 15px;
  overflow: hidden;
  .messege-content {
    width: 100%;
    height: 100%;
    overflow: hidden;
    padding-top: 4px;
    ul {
      width: 100%;
      height: 100%;
      position: relative;
      transition: margin-top 0.5s;

      li {
        width: 100%;
        height: 1/10 * 100%;
        padding-bottom: 5px;
        overflow: hidden;
        a {
          padding-top: 17px;
          width: 100%;
          height: 100%;
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          color: #d3d3d3;
          text-decoration: none;
          background: rgba(216, 191, 216, 0.1);
          border-radius: 5px;
          padding-left: 25px;
          position: relative;
          font-size: 1.9rem;
          line-height: 2rem;
          &:before {
            //padding-top: 25px;
            position: absolute;
            box-sizing: border-box;
            margin-left: -22px;
            display: block;
            height: 100%;
            content: "◆";
            font-size: 4.5rem;
            color: #eb8a00;
          }
          i {
            padding-right: 8px;
          }
        }
      }
    }
  }
}
</style>
