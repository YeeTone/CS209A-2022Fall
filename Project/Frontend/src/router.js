import Vue from "vue";
import Router from "vue-router";
import data from "@/views/data";

Vue.use(Router);

export default new Router({
  base: process.env.BASE_URL,
  routes: [
    // {
    //   path: "/",
    //   name: "index",
    //   component: index
    //  },
    // {
    //   path: "/data",
    //   name: "data/",
    //   component: index
    // },
    {
      path: "/",
      name: "data",
      component: data
    }
  ]
});
