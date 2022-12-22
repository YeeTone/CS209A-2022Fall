<template>

</template>

<script>
export default {
  name: "repoInfo",
  shareName:"redis-py",
  shareOwner:"redis",
  changeName(owner,name){
    this.shareName=name
    this.shareOwner=owner
    console.log("share")
    console.log(this.shareName)
    console.log(this.shareOwner)
  },
  getShare(){
    return {
      repo_owner:this.shareOwner,
      repo_name:this.shareName
    }
  }
}
</script>

<style scoped>

</style>