<template>
  <div class="right-content">
    <div class="top-box">
        <databox
            :title="$t('data.dright.index.developerInfo')"
            :dheight="880"
            :icon="'account'"
        >
          <message></message>

        </databox>
    </div>
  </div>
</template>

<script>
import follower from "./follower";
import message from "./message";
import Dnumber from "@/views/data/dcontent/dnumber/index.vue";
import repoInfo from "@/views/data/repoInfo.vue";
export default {
  components: {
    follower,
    Dnumber,
    message,
  },
  props: {
    username: String
  },
  data() {
    return {  repoInfo,
      repo_owner:"redis",
      repo_name:"redis-py",
      numDeveloper: 4,

    };
  },
  created() {
    this.getNumDeveloper()
    this.getChange()
  },
  methods: {getChange(){
      setInterval(()=>{

        if(repoInfo.shareName!==this.repo_name){
          this.repo_name=repoInfo.shareName
          this.repo_owner=repoInfo.shareOwner
          this.getNumDeveloper()
        }

      },5)

    },
    getNumDeveloper() {
      // console.log("getNumRelease")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/mostActiveTop', {
            params: {}
          })
          .then(function (response) {
            _this.numDeveloper = response.data
            // console.log(response);
          })
          .catch(function (error) {
            // console.log(error);
          });
    },

  },
  watch: {
    username(username) {
      if (username) {
        this.getData(username);
      }
    }
  }
};
</script>

<style lang="scss">
.right-content {
  width: 100%;
  height: 100%;

  .top-box {
    padding-bottom: 40px;




  }
}
</style>
