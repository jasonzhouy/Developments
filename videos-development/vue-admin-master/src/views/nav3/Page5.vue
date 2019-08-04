<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="姓名"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getVideos">查询</el-button>
				</el-form-item>
			</el-form>
		</el-col>
    <!--列表-->
	<el-table :data="videos" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index"  v-if="false" >
			</el-table-column>
			<el-table-column prop="coverPath" label="视频" width="120" max-height="120" align="center" sortable>
				<template  slot-scope="scope" >            
                    <img :src= "baseUrl + scope.row.coverPath"  min-width="70" height="70"  @click="showVideo(scope.row.videoPath)"/>
                 </template>
			</el-table-column>
			<el-table-column prop="tag" label="标签" width="100" sortable>
			</el-table-column>
			<el-table-column prop="bgmName" label="背景音乐" width="120" sortable>
			</el-table-column>
			<el-table-column prop="username" label="用户名" width="100" sortable>
			</el-table-column>
			<el-table-column prop="videoDesc" label="视频描述" width="120" sortable>
			</el-table-column>
			<el-table-column prop="likeCounts" label="喜欢" min-width="120" sortable>
			</el-table-column>
			<el-table-column prop="status" label="播放状态"  :formatter="formatStatus" min-width="120" sortable>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" :formatter="formatTime" min-width="180" sortable>
			</el-table-column>
			
		</el-table>


		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
			<el-pagination layout="prev, pager, next" style="float:right;" @current-change="handleCurrentChange" :page-size= "5" :total= "total*5" >
			</el-pagination>
		</el-col>


  </section>
</template>

<script>
import util from "../../common/js/util";
//import NProgress from 'nprogress'
import { getVideoListPage, removeVideo,batchRemoveVideos } from "../../api/api";
export default {
		data() {
			return {
				filters: {
					name: ''
				},
				videos: [],
				baseUrl: 'https://www.zymonster.com/v',
				total: 50,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列


			}
		},
		methods: {

			formatStatus: function (row, column) {
				return row.status == 1 ? '可播放' : row.status == 2 ? '被禁止' : '被删除';
			},
			formatTime: function (row, column) {
				return util.formatDate.format(new Date(row.createTime),'yyyy-MM-dd hh:mm')
			},

			showVideo(param) {

				
				// location.href = this.baseUrl + param;

				window.open(this.baseUrl + param);

			},
			
			//获取用户列表
			getVideos() {
				let para = {
					page: this.page,
					name: this.filters.name
				};
				this.listLoading = true;
				//NProgress.start();
				getVideoListPage(para).then((res) => {
					console.log(res.data.data);
					this.total = res.data.data.total;
					this.videos = res.data.data.rows;
					this.listLoading = false;
					//NProgress.done();
				});
			},

			selsChange: function (sels) {
				this.sels = sels;
			},

			//批量删除
			batchRemove: function () {
				var ids = this.sels.map(item => item.id).toString();
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = { ids: ids };
					console.log(ids);
					batchRemoveVideos(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						this.getVideos();
					});
				}).catch(() => {

				});
			},

			handleCurrentChange(val) {
				this.page = val;
				this.getVideos();
			}
			
			
			
			
			
		},

		
		mounted() {
			this.getVideos();
		}
	}

</script>

<style scoped>

</style>