<template>
  <div class="myevent-box">
    <table class="table">
      <thead>
      <tr>
        <th>{{ $t("data.myevent.table.average") }}</th>
        <th>{{ $t("data.myevent.table.range") }}</th>
        <th>{{ $t("data.myevent.table.variance") }}</th>

      </tr>
      </thead>
      <tbody>
      <tr>
        <td>{{ average }}</td>
        <td>{{ range }}</td>
        <td>{{ variance }}</td>

      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import repoInfo from "@/views/data/repoInfo.vue";

export default {

  data() {
    return {
      repoInfo,
      repo_owner: "redis",
      repo_name: "redis-py",
      average: 2,
      range: 3,
      variance: 4

    };
  }, created() {
    this.getAverage(),
        this.getVariance(),
        this.getRange()
    this.getChange()
  },
  methods: {
    getChange() {
      setInterval(() => {

        if (repoInfo.shareName !== this.repo_name) {
          this.repo_name = repoInfo.shareName
          this.repo_owner = repoInfo.shareOwner
          this.getAverage(),
              this.getVariance(),
              this.getRange()
        }

      }, 5)

    },
    getAverage() {
      console.log("getIssueOpen")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/' + _this.repo_owner + '/' + _this.repo_name + '/avgIssueResolutionTime', {
            params: {}
          })
          .then(function (response) {
            _this.average = response.data.average.toFixed(2)
            // console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
    },

    getRange() {
      console.log("getIssueOpen")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/' + _this.repo_owner + '/' + _this.repo_name + '/rangeIssueResolutionTime', {
            params: {}
          })
          .then(function (response) {
            _this.range = response.data.range
            // console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
    },

    getVariance() {
      console.log("getIssueOpen")
      const _this = this
      this.$axios.get(
          'http://localhost:8081/' + _this.repo_owner + '/' + _this.repo_name + '/varIssueResolutionTime', {
            params: {}
          })
          .then(function (response) {
            _this.variance = response.data.variance.toFixed(2)
            // console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
    }
  }


};
</script>

<style lang="scss">
.myevent-box {
  width: 100%;
  height: 100%;
  padding: 15px;
  overflow: hidden;
  margin-top: -10px;

  .table {
    table-layout: fixed;

    tr {
      border: none;

      th {
        font-size: 1.5rem;
        text-align: center;
        background: #053a98;
        color: #fffdef;
        border: 1px solid #0086B3;
      }

      td {
        font-size: 1.4rem;
        text-align: center;
        border: 1px solid #0086B3;
        color: #19d4ae;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      &:nth-child(odd) {
        td {
          color: #5ab1ef;
        }
      }
    }
  }
}
</style>
