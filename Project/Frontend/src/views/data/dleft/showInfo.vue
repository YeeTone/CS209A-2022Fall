<template>
  <div class="number-box" :style="{ height: dheight + 'px' }">
<!--    <div class="icon-box">-->
<!--      <icon :type="icon" :color="color"></icon>-->
<!--    </div>-->
    <div class="number-box">
      <div class="title">{{ title }}</div>
      <div class="number" :style="styles">{{info  }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "dnumber",
  props: {
    size: [Number, String],
    time: {
      type: Number,
      default: 2
    },
    title: String,
    color: String,
    dnumber: {
      type: Number,
      default: 0
    },
dinfo:"",
    icon: String,
    dheight: Number
  },
  data() {
    return {
      info:"huhu",
      amount: 0
    };
  },
  watch: {

    dinfo(val){
      this.info=val
    }

  },

  computed: {
    styles() {
      const style = {};

      if (this.color) {
        style.color = this.color;
      }


      return style;
    }
  }
};
</script>

<style lang="scss">
.number-box {
  width: 100%;
  height: 100%;
  display: flex;

  .number-box {
    width: 100%;
    float: left;
    display: block;
    .title {
      width: 100%;
      text-align: center;
      font-size: 1.6rem;
      color: #fff;
      padding-top: 10px;
      line-height: 3rem;
    }
    .number {text-align: center;
      padding-top: 10px;
      width: 100%;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: normal;
      font-weight: bold;
      font-size: 1vw;
    }
  }
}
</style>
