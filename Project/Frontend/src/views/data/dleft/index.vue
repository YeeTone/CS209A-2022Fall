<template>

  <div class="left-content">

    <div class="top-box">
      <div class="short">
        <div class="ss">
          <dnumber
              :dheight="110"
              :title="$t('data.myevent.numOpenIssue')"
              :size="'4rem'"
              :dnumber="openIssue"
              :icon="'kucunguanli'"
              :color="'#ffff43'"
          >
          </dnumber>
        </div>
      </div>

      <div class="short">
        <div class="ss">
          <dnumber
              :dheight="110"
              :title="$t('data.myevent.numCloseIssue')"
              :size="'4rem'"
              :dnumber="closeIssue"
              :icon="'jindu1'"
              :color="'#25f3e6'"
          >
          </dnumber>


        </div>
      </div>
      <div class="short">
        <div class="ss">
          <dnumber
              :dheight="110"
              :title="$t('data.myevent.numRelease')"
              :size="'4rem'"
              :dnumber="numRelease"
              :icon="'kucunguanli'"
              :color="'#ff5496'"
          >
          </dnumber>
        </div>
      </div>

      <div class="short">
        <div class="ss">
          <dnumber
              :dheight="110"
              :title="$t('data.myevent.numDeveloper')"
              :size="'4rem'"
              :dnumber="numDeveloper"
              :icon="'kucunguanli'"
              :color="'#8afa45'"
          >
          </dnumber>


        </div>
      </div>
      <div class="long">
        <div class="ll">
          <show-info
              :dheight="110"
              :title="$t('data.myevent.mostActive')"
:dinfo="mostActiveAuthor"
              :icon="'kucunguanli'"
              :color="'#e6f367'"
          >
          </show-info>
        </div>
      </div>

      <div class="short">
        <div class="ss">
          <dnumber
              :dheight="110"
              :title="$t('data.myevent.commitTimes')"
              :size="'4rem'"
              :dnumber="commitTimes"
              :icon="'kucunguanli'"
              :color="'#d5e352'"
          >
          </dnumber>


        </div>
      </div>



    </div>

    <div class="left-box">

      <databox :title="''" :dheight="770" >
        <div style="display: flex; flex-direction: row;" class="linecss">
        <div id="chartLine" class="line-wrap"></div>
        <div id="chartLine2" class="line-wrap"></div>
        </div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" >
          <el-form-item label="    ">
            <el-input v-model="firstRelease" :placeholder=holder></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">search</el-button>
          </el-form-item>
        </el-form>
          <div id="chartLine3" class="line-wrap">


          </div>

      </databox>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

require('echarts/theme/macarons');//引入主题
require('echarts/theme/shine');//引入主题
import personal from "./personal";
import Dnumber from "@/views/data/dcontent/dnumber/index.vue";
import repoInfo from "@/views/data/repoInfo.vue";
import showInfo from "@/views/data/dleft/showInfo.vue";
export default {
  components: {
    Dnumber,
    personal,
    showInfo
  },
  props: {
    personalData: Object,
    username: String
  },
  data() {
    this.extend = {
      legend: {
        textStyle: {color: "#fff"}
      },
      grid: {
        textStyle: {
          color: "#fff"
        }
      },
      series: {
        radius: ["0", "40%"],
        center: ["50%", "50%"]
      }
    };
    return {
      numRelease: 4000,
      holder:"the first ? release ",
      firstRelease:"",
      openIssue: null,
      closeIssue: null,
      mostActiveAuthor:"",
      commitTimes:0,
      numDeveloper: 4,repo_owner:"redis",
      repo_name:"redis-py",
      topRelease:10,
      chartPie: null,
      chartLine: null,
      dayTime: ['morning', 'forenoon', 'noon', 'afternoon', 'evening', 'night', 'midnight'],
      dayTime2: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
      releaseTime: [{
        release_name: "",
        published_at: 1
      }],
      releaseName:[],
      commitNum: [1, 2, 1, 1, 1, 1, 1],
      commitNum2: [1, 2, 1, 1, 1, 1, 1],
      releaseNum: [],

      personalD: {},
      starData: {
        columns: ["reposName", "getStars"],
        rows: []
      },
      noStarData: false,
      languageData: {
        columns: ["lang", "number"],
        rows: []
      },
      nolanguageData: false
    };
  },
  created() {
    this.getNumRelease()
    // this.initialCommitNum()
    this.getCommitNum();
    this.getCommitNum2();
    this.getReleaseNum();
    this.getChange()
    this.getNumDeveloper()
    this.getMostActive()
    this.getIssueOpen()
    this.getIssueClosed()
  }, watch: {
    commitNum: function () {
      // console.log("watch")
      this.$nextTick(() => {
        // this.drawPieChart();
        this.drawLineChart();

      })
    },
    commitNum2: function () {
      // console.log("watch")
      this.$nextTick(() => {
        // this.drawPieChart();
        this.drawLineChart2();
      })
    },
    releaseNum: function () {
      // console.log("watch")
      this.$nextTick(() => {
        // this.drawPieChart();
        this.drawLineChart3();
      })
    }

  },

  methods: {
    onSubmit() {

      let _this = this
      if(_this.firstRelease>_this.numRelease){
        this.$notify.error({
          title: '错误',
          message: 'the number is larger than the release number'
        });
        return
      }
      if(!(_this.firstRelease<_this.numRelease&&_this.firstRelease>0)){
        this.$notify.error({
          title: '错误',
          message: 'number only'
        });
        return
      }
      _this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/releaseTimeStampTop', {
            params: {
              top: _this.firstRelease+""
            }
          })
          .then(function (response) {
            _this.releaseTime = response.data

          }).then(
          function (){
            console.log("xunhunakaishi")
            _this.releaseNum=[]
            _this.releaseName=[]
            for (let i = 0; i < _this.firstRelease-1; i++) {
              _this.$axios.get(
                  'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/commitBetween', {
                    params: {
                      upper: _this.releaseTime[i].published_at,
                      lower: _this.releaseTime[i + 1].published_at
                    }
                  })
                  .then(function (response) {
                    _this.releaseNum.push(response.data)
                    _this.releaseName.push(_this.releaseTime[i].release_name)
                    // _this.$set(_this.releaseNum, i, response.data)
                    console.log("_this.releaseNum----------------")
                    console.log(response.data)
                    // _this.commitNum[i]=response.data
                  })
                  .catch(function (error) {
                    // console.log(error);
                  });
            }}
      )
          .catch(function (error) {
            // console.log("errpr")
            // console.log(error);
          });
    },
    getChange() {
      setInterval(() => {

        if (repoInfo.shareName !== this.repo_name) {
          this.repo_name = repoInfo.shareName
          this.repo_owner = repoInfo.shareOwner
          this.getNumRelease()
          // this.initialCommitNum()
          this.getCommitNum();
          this.getCommitNum2();
          this.getReleaseNum();
          this.getNumDeveloper()
          this.getMostActive()
          this.getIssueOpen()
          this.getIssueClosed()

        }

      }, 5)

    },getIssueOpen() {
      console.log("getIssueOpen")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/issueCount', {
            params: {
              state: "open"
            }
          })
          .then(function (response) {
            _this.openIssue = response.data
            console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
    },
    getIssueClosed() {
      console.log("getIssueClosed")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/issueCount', {
            params: {
              state: "closed"
            }
          })
          .then(function (response) {
            _this.closeIssue = response.data
            console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
    },
    getMostActive() {
      // console.log("getNumRelease")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/mostActiveTop', {
            params: {top:"1"}
          })
          .then(function (response) {
            _this.mostActiveAuthor = response.data[0].author
            console.log("_this.mostActiveAuthor")
            console.log(_this.mostActiveAuthor)
            _this.commitTimes=response.data[0].cnt
            // console.log(response);
          })
          .catch(function (error) {
            // console.log(error);
          });
    },

    getNumDeveloper() {
      // console.log("getNumRelease")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/developerCount', {
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
    getReleaseNum() {
      let _this = this
      _this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/releaseTimeStampTop', {
            params: {
              top: _this.topRelease+""
            }
          })
          .then(function (response) {
            _this.releaseTime = response.data

          }).then(
              function (){
                console.log("xunhunakaishi")
                _this.releaseNum=[]
                _this.releaseName=[]
                for (let i = 0; i < _this.topRelease-1; i++) {
                  _this.$axios.get(
                      'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/commitBetween', {
                        params: {
                          upper: _this.releaseTime[i].published_at,
                          lower: _this.releaseTime[i + 1].published_at
                        }
                      })
                      .then(function (response) {
                        _this.releaseNum.push(response.data)
                        _this.releaseName.push(_this.releaseTime[i].release_name)
                        // _this.$set(_this.releaseNum, i, response.data)
                        console.log("_this.releaseNum----------------")
                        console.log(response.data)
                        // _this.commitNum[i]=response.data
                      })
                      .catch(function (error) {
                        // console.log(error);
                      });
              }}
      )
          .catch(function (error) {
            // console.log("errpr")
            // console.log(error);
          });

    },
    getCommitNum() {
      // console.log("getCommitNum")
      const _this = this
      for (let i = 0; i < 7; i++) {
        this.$axios.get(
            'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/dayTimeLabel', {
              params: {
                label: _this.dayTime[i]
              }
            })
            .then(function (response) {
              _this.$set(_this.commitNum, i, response.data)
              // _this.commitNum[i]=response.data
            })
            .catch(function (error) {
              // console.log(error);
            });
      }

      // console.log("commitNum")
      // console.log(_this.commitNum)


    },
    getCommitNum2() {
      // console.log("getCommitNum")
      const _this = this
      for (let i = 0; i < 7; i++) {
        this.$axios.get(
            'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/weekTimeLabel', {
              params: {
                label: _this.dayTime2[i]
              }
            })
            .then(function (response) {
              _this.$set(_this.commitNum2, i, response.data)
              // _this.commitNum[i]=response.data
            })
            .catch(function (error) {
              // console.log(error);
            });
      }

      // console.log("commitNum2")
      // console.log(_this.commitNum2)


    },

    drawLineChart() {
      const _this = this
      this.chartLine = echarts.init(document.getElementById('chartLine'), 'shine');// 基于准备好的dom，初始化echarts实例

      let option = {
        tooltip: {
          trigger: 'axis',

        },
        color:['#f52dc6','#ff6600','#808bc6'],
        legend: {

          data: ['commit times by time'],
          textStyle: {
            fontSize: 17,
            color: '#ff0505'
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisTick: {
              show: false
            },
            data: ['morning', 'forenoon', 'noon', 'afternoon', 'evening', 'night', 'midnight'],
            axisLabel: {
              show: true,
              textStyle: {
                color: '#2fdada' //这里用参数代替了
              }
            },
            axisLine: {
              //坐标轴颜色
              lineStyle: {
                color: "#2fdada",
              },
            },

          }
        ],
        yAxis: [
          {
            type: 'value',
            axisTick: {
              show: false
            },
            name: 'unit: times',

            axisLabel: {
              show: true,
              textStyle: {
                color: '#2fdada' //这里用参数代替了
              },


            },
            axisLine: {
              //坐标轴颜色
              lineStyle: {
                color: "#2fdada",
              },
            },
            splitLine: {
              //网格区域
              show: true,
              lineStyle: {
                color: "rgba(255,255,255,0.93)",
                width: 1,
                type: "solid",
              },
            }
          }
        ],
        series: [
          {
            name: 'commit times by time',
            type: 'line',
            stack: '总量',
            data: _this.commitNum,
            itemStyle : {
              normal : {
                color: "#ff0505", //改变折线点的颜色
                lineStyle:{
                  color:'#ff0505'
                }
              }
            }
          }

        ]
      };


      // console.log("option")
      // console.log(option)
      // 使用刚指定的配置项和数据显示图表
      this.chartLine.setOption(option);

    }, drawLineChart2() {
      const _this = this
      this.chartLine = echarts.init(document.getElementById('chartLine2'), 'shine');// 基于准备好的dom，初始化echarts实例

      let option = {
        tooltip: {
          trigger: 'axis'
        }, color:['#f52dc6','#ff6600','#808bc6'],
        legend: {
          data: ['commit times by day'],
          textStyle: {
            fontSize: 17,
            color: '#ff0505'
          }
        },
        calculable: true,
        xAxis: [
          {

            type: 'category',
            boundaryGap: false,
            axisTick: {
              show: false
            },
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisLabel: {
              show: true,
              textStyle: {
                color: '#2fdada' //这里用参数代替了
              }
            },
            axisLine: {
              //坐标轴颜色
              lineStyle: {
                color: "#2fdada",
              },
            },
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisTick: {
              show: false
            },
            name: 'unit: times',
            axisLabel: {
              show: true,
              textStyle: {
                color: '#2fdada' //这里用参数代替了
              },


            },
            axisLine: {
              //坐标轴颜色
              lineStyle: {
                color: "#2fdada",
              },
            },
            splitLine: {
              //网格区域
              show: true,
              lineStyle: {
                color: "rgba(255,255,255,0.93)",
                width: 1,
                type: "solid",
              }
          }}
        ],
        series: [
          {
            name: 'commit times by day',
            type: 'line',
            stack: '总量',
            data: _this.commitNum2,
            itemStyle : {
              normal : {
                color: "#ff0505", //改变折线点的颜色
                lineStyle:{
                  color:'#ff0505'
                }
              }
            }
          }

        ]
      };


      // console.log("option")
      // console.log(option)
      // 使用刚指定的配置项和数据显示图表
      this.chartLine.setOption(option);

    }, drawLineChart3() {
      const _this = this
      this.chartLine = echarts.init(document.getElementById('chartLine3'), 'shine');// 基于准备好的dom，初始化echarts实例

      let option = {
        tooltip: {
          trigger: 'axis'
        }, color:['#f52dc6','#ff6600','#808bc6'],
        legend: {
          data: ['commit times between release'],
          textStyle: {
            fontSize: 17,
            color: '#ff0505'
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            axisTick: {
              show: false
            },
            data: _this.releaseName,
            axisLabel: {
              show: true,
              textStyle: {
                color: '#2fdada' //这里用参数代替了
              }
            },
            axisLine: {
              //坐标轴颜色
              lineStyle: {
                color: "#2fdada",
              },
            },
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisTick: {
              show: false
            },
            name: 'unit: times',
            axisLabel: {
              show: true,
              textStyle: {
                color: '#2fdada' //这里用参数代替了
              },


            },
            axisLine: {
              //坐标轴颜色
              lineStyle: {
                color: "#2fdada",
              },
            },
            splitLine: {
              //网格区域
              show: true,
              lineStyle: {
                color: "rgba(255,255,255,0.93)",
                width: 1,
                type: "solid",
              }}
          }
        ],
        series: [
          {
            name: 'commit times between release',
            type: 'line',
            stack: '总量',
            data: _this.releaseNum,
            itemStyle : {
              normal : {
                color: "#ff0505", //改变折线点的颜色
                lineStyle:{
                  color:'#ff0505'
                }
              }
            }
          }

        ]
      };


      // console.log("option")
      // console.log(option)
      // 使用刚指定的配置项和数据显示图表
      this.chartLine.setOption(option);

    },
    drawPieChart() {
      let mytextStyle = {
        color: "#333",
        fontSize: 18,
      };
      let mylabel = {
        show: true,
        position: "right",
        offset: [30, 40],
        formatter: '{b} : {c} ({d}%)',
        textStyle: mytextStyle
      };
      this.chartPie = echarts.init(document.getElementById('chartPie'), 'macarons');
      this.chartPie.setOption({
        title: {
          text: 'Pie Chart',
          subtext: '纯属虚构',
          x: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        legend: {
          data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎'],
          left: "center",
          top: "bottom",
          orient: "horizontal",
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['50%', '50%'],
            data: [
              {value: 335, name: '直接访问'},
              {value: 310, name: '邮件营销'},
              {value: 234, name: '联盟广告'},
              {value: 135, name: '视频广告'},
              {value: 1548, name: '搜索引擎'}
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            label: {
              emphasis: mylabel
            }
          }
        ]
      });
    },
    getNumRelease() {
      // console.log("getNumRelease")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/'+_this.repo_owner+'/'+_this.repo_name+'/releaseCount', {
            params: {
              state: "closed"
            }
          })
          .then(function (response) {
            _this.numRelease = response.data
            // console.log(response);
          })
          .catch(function (error) {
            // console.log(error);
          });
    },
    getData(username) {
      this.$axios
          .get("/api/users/" + username + "/repos")
          .then(response => {
            let data = JSON.parse(JSON.stringify(response.data));
            if (data.length < 1) {
              this.noStarData = true;
            } else {
              let rowdata = [];
              let languages = {};
              for (var i = 0; i < data.length; i++) {
                // 仓库和仓库star
                let reposName = data[i].name;
                let getStars = data[i].stargazers_count;
                let obj = {
                  reposName: reposName,
                  getStars: getStars
                };
                rowdata.push(obj);

                //梳理语言，计算语言类型和各个语言的数量
                let langData = data[i].language;
                if (langData) {
                  if (langData in languages) {
                    languages[langData]++;
                  } else {
                    languages[langData] = 1;
                  }
                }
              }
              //把值附到starData.rows
              this.starData.rows = rowdata;
              //console.log(this.starData.rows)

              //重新组装语言封成对象
              let objL = JSON.parse(JSON.stringify(languages));
              let dataL = [];
              for (var key in objL) {
                dataL.push({lang: key, number: objL[key]});
              }
              this.languageData.rows = dataL;
              //console.log(objL)
              //console.log(this.languageData.rows)
            }
            return;
          })
          .catch(err => {
            console.log(err.message);
          });
    }
  }
  // watch: {
  //   username(username) {
  //     if (username) {
  //       this.getData(username);
  //     }
  //   }
  // }
};
</script>

<style lang="scss">
.left-content {

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

    .pie-wrap {
      width: 100%;
      height: 400px;
    }

    .line-wrap {
      width: 100%;
      height: 360px;
padding-top: 10px;

   }
    .demo-form-inline{
      padding-top: 10px;
      padding-left: 20px;
    }
    .linecss{
      //background: #19d4ae;
      color:#2ad3e3;
    }

  }

  .top-box {
    display: flex;
    width: 100%;
    height: 110px;
    overflow: hidden;
    .short {
      z-index: 100;
      flex: 1;
      padding-right: 7px;
      padding-left: 7px;
      .ss {

        width: 100%;
        background: rgba(35, 72, 135, 0.4);
      }

    }
    .long {
      z-index: 100;
      flex: 1;
      padding-right: 7px;
      padding-left: 7px;
      .ll {

        width: 100%;
        background: rgba(35, 72, 135, 0.4);
      }

    }

  }

}

</style>
