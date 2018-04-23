<template>
  <div>
        未完成
<el-tree
  :data="data2"
  show-checkbox
  node-key="id"
  :default-expanded-keys="[2, 3]"
  :default-checked-keys="[5]"
  :props="defaultProps">
</el-tree>


      </div>
</template>
<script>
export default {
    data() {
        return {
                 data2: [{
          id: 1,
          label: '一级 1',
          children: [{
            id: 4,
            label: '二级 1-1',
            children: [{
              id: 9,
              label: '三级 1-1-1'
            }, {
              id: 10,
              label: '三级 1-1-2'
            }]
          }]
        }, {
          id: 2,
          label: '一级 2',
          children: [{
            id: 5,
            label: '二级 2-1'
          }, {
            id: 6,
            label: '二级 2-2'
          }]
        }, {
          id: 3,
          label: '一级 3',
          children: [{
            id: 7,
            label: '二级 3-1'
          }, {
            id: 8,
            label: '二级 3-2'
          }]
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    computed: {
    filter_user_list() {
      var self = this;
      let department_id = localStorage.getItem('department_id');
      let ms_username= localStorage.getItem('ms_username');
      var filter_user_list = new Array();
      for (var i in self.user_list) {
        if (self.user_list[i].department == department_id & self.user_list[i].chinese_name != ms_username) {
          filter_user_list.push(self.user_list[i])
        }
      }
      return filter_user_list
    }
  },
    created() {
                this.get_Structure();
        this.get_sale_target();

//        this.get_event_types()
//        this.get_sale_event_types()
    },
    methods: {
            dateChange1(val) {
      var self = this;
      self.filters.naturalWeek = val
    },
        handleEdit: function (index, row) {
            this.editFormVisible = true;
            this.editForm = Object.assign({}, row);
        },
            filter: function (params) {
      this.sale_target = [];
      this.get_sale_target();

    },
        get_Structure: function (params) {
      var self = this;
      this.$axios.get('/handle_auth/retrieve_department/')
        .then(function (response) {
          self.data2 = eval(response.data.content);
        }
        );

    },
         get_sale_target: function (params) {
      var self = this;
//      var employee_name = localStorage.getItem("ms_username")
      this.$axios.get('/analysis/display_sale_target/', {
        params: {
          natural_week: self.filters.naturalWeek,
//          employee_name: employee_name,
        }
      })
        .then(function (response) {
          var responseContent = JSON.parse(response.data.content);
          function combineCell(list) {
            var k = 0;
            while (k < list.length) {
              list[k]['span'] = 1;
              list[k]['dis'] = false;
              for (var i = k + 1; i <= list.length - 1; i++) {
                if (list[k]["visit_date"] == list[i]["visit_date"] && list[k]["visit_date"] != '') {
                  list[k]['span']++;
                  list[k]['dis'] = false;
                  list[i]['span'] = 1;
                  list[i]['dis'] = true;
                  // list[k]['total_time'] += list[i]['duration_time'];
                } else {
                  break;
                }
              }
              k = i;
            }
            return list;
          }
          self.sale_event_list = combineCell(responseContent)
            self.sale_target=response.data.content;
            console.log(self.sale_target)
        }
        );
    },
    },
}
</script>
