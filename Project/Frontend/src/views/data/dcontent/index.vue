<template>
  <div class="content-cont">



    <div class="left-box">
      <databox :title="''" :dheight="880">
      <databox
          :title="$t('data.myevent.myevent')"
          :dheight="150"
          :icon="'account'"
          :boxb="false"
      >

        <myevent ></myevent>

      </databox>
      <databox
          :title="$t('data.dleft.accountStars')"
          :dheight="400"
          :icon="'account'"
          :boxb="false"
      >
        <ve-pie :data="starData" :extend="extend" :height="'300px'"></ve-pie>

      </databox>
        <div class="cloud-chart">
          <div id="wordCloudChart" style="height: 255px; "></div>
        </div>
      </databox>



    </div>

  </div>
</template>

<script>
import dnumber from "./dnumber";
import myevent from "./myevent";
import {LINE_DATA} from "../test/data";
import * as echarts from "echarts"
import 'echarts-wordcloud/dist/echarts-wordcloud'
import 'echarts-wordcloud/dist/echarts-wordcloud.min'
import repoInfo from "@/views/data/repoInfo.vue";
export default {
  components: {
    dnumber,
    myevent
  },
  props: {
    numberData: Object,
    username: String
  },
  data() {
    this.extend = {
      series: {
        label: {
          normal: {
            show: true
          }
        }
      },
      legend: {
        textStyle: {color: "#fff"},
        right: "6%"
      },
      yAxis: {
        axisLabel: {
          textStyle: {
            color: "#fff"
          }
        }
      },
      xAxis: {
        axisLabel: {
          textStyle: {
            color: "#fff"
          }
        }
      }
    };

    return {
      starData: {
        columns: ["word", "cnt"],
        rows: []
      },
      repoInfo,
      repo_owner:"redis",
      repo_name:"redis-py",
      openIssue: null,
      closeIssue: null,
      seriesData:  [ ],

      chartData: LINE_DATA,
      myevent: {
        average:2,
        range:3,
        variance:5

      },
      noMyevent: false,
      repoData: {
        columns: ["reposName", "size", "forks"],
        rows: []
      },
      noRepoSize: false
    };
  },
  created() {
    this.getIssueTopics()
    this.getChange()
    this.getIssueTopics2()
  }, mounted() {

  },
  methods: {
    getChange(){
      setInterval(()=>{

        if(repoInfo.shareName!==this.repo_name){
          this.repo_name=repoInfo.shareName
          this.repo_owner=repoInfo.shareOwner
          this.getIssueTopics()
          this.getIssueTopics2()
        }

      },5)

    },
    getIssueTopics2() {
      console.log("getIssueOpen")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/gitHotWordTop', {
            params: {
              top: 15
            }
          })
          .then(function (response) {
            _this.starData.rows=[]
            for (let i = 0; i <10 ; i++) {
              _this.starData.rows.push({
                word:response.data[i].name,
                cnt:response.data[i].value
              })
            }

          })
          .catch(function (error) {
            console.log(error);
          });
    },
    getIssueTopics() {
      console.log("getIssueTopics")
      const _this = this

      this.$axios.get(

          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/gitHotWordTop', {
            params: {
              top: 100
            }
          })
          .then(function (response) {

            _this.seriesData=response.data



            _this.seriesData.forEach((e, i) => {
              _this.seriesData[i].textStyle = {
                normal: {
                  color: 'rgb(' + [
                    Math.round(Math.random() * 400),
                    Math.round(Math.random() * 400),
                    Math.round(Math.random() * 400)
                  ].join(',') + ')'
                }
              }
            })

            const wordOpt = {

              series: [
                {
                  type: 'wordCloud',
                  shape: 'circle',
                  left: 'center',
                  top: 0,
                  width: '90%',
                  height: '100%',
                  sizeRange: [10, 60],
                  rotationRange: [0, 0],
                  gridSize: 16,
                  drawOutOfBound: false,
                  data: _this.seriesData,


                },
              ]
            }
            _this.wordChart = echarts.init(
                document.getElementById('wordCloudChart')
            )
            _this.wordChart.setOption(wordOpt)

          })

          .catch(function (error) {
            console.log(error);
          });
      // this.setWordCloud()
    },

  }

,
watch: {
repoInfo:function (){
  console.log("dcontent")
}
}
}
;
</script>

<style lang="scss">
.content-cont {
  width: 100%;
  height: 100%;
  .left-box {
    width: 100%;
    height: 720px;

    padding-bottom: 40px;
    .content-wapper {
      padding-top: 40px;
      height: 220px;
    }
  }
  .top-box {
    display: flex;
    width: 100%;
    height: 110px;
    overflow: hidden;

    .left {
      text-align: center;
      flex: 1;
      padding-right: 7px;
      padding-left: 7px;
      .leftcontent {
        text-align: center;
        width: 100%;
        background: rgba(35, 72, 135, 0.4);
      }

    }

    .right {
      padding-left: 7px;
      flex: 1;
      padding-right: 7px;text-align: center;
      .rightcontent {
        text-align: center;
        width: 100%;
        background: rgba(35, 72, 135, 0.4);
      }

    }
  }

  .content-box {
    padding-top: 10px;

  }

  .cloud-chart {


    padding-top: 10px;
  }
  .chart {
    width: 10%;
    height: 300px;
  }

}
</style>
