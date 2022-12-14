package com.example.recruitmentofprojectteammembers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recruitmentofprojectteammembers.databinding.ActivityDetailPostBinding
import data.*
import network.RetrofitClient.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding: ActivityDetailPostBinding
lateinit var recyclerAdapterDP : RecyclerAdapterDP
var resultList : Reply = Reply()
var postId : Int = 0

class DetailPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        var postTitle = intent.getStringExtra("title")
        postId = intent.getIntExtra("post_id", -1)
        var usrId = intent.getIntExtra("create_member_id", -1)

        var replyContent : String

        // 댓글 리스트 초기화 하기
        resultList.clear()

        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러뷰 어댑터 선언
        binding.dpReplyRecycler.layoutManager = LinearLayoutManager(this@DetailPostActivity)
        recyclerAdapterDP = RecyclerAdapterDP()
        binding.dpReplyRecycler.adapter = recyclerAdapterDP
        // 리사이클러뷰 아이템 공백 설정 클래스 적용
        binding.dpReplyRecycler.addItemDecoration(recyclerDecoration(40))

        // 게시물 상세정보 불러오기
        retrofitService.requestDetailPost(postId).enqueue(object : Callback<SrhPostModelItem>{
            override fun onResponse(call: Call<SrhPostModelItem>, response: Response<SrhPostModelItem>) {
                var detailPost : SrhPostModelItem? = response.body()
                if (detailPost != null) {
                    binding.dpContent.text = detailPost.content + "\n"
                    binding.dpTitle.text = postTitle

                }
            }

            override fun onFailure(call: Call<SrhPostModelItem>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        binding.dpTvusrname.text = "개발자 ${usrId}"

        Log.d("tag113", "댓글 동작 확인")
        Log.d("tag113", postId.toString())

        // 댓글 입력할 때
        binding.dpReplyEdt.setOnClickListener(){

        }

        // 댓글 등록 버튼 클릭한 경우
        binding.dpReplyBtn.setOnClickListener(){
            replyContent = binding.dpReplyEdt.text.toString()

            // 빈 값으로 댓글 등록을 시도하는 경우
            if(binding.dpReplyEdt.text.toString() == ""){
                var dialog = AlertDialog.Builder(this@DetailPostActivity)
                dialog.setTitle("내용을 입력하세요.")
                dialog.setMessage("")
                dialog.show()
            }
            else{

                //  댓글 달기
                if(postId != null){
                    retrofitService.requestReply(ReplyData(loginResponse.member_id, postId, replyContent), postId).enqueue(object : Callback<CommentPostStatus> {
                        override fun onResponse(call: Call<CommentPostStatus>, response: Response<CommentPostStatus>, ) {

                            // 댓글 달기 성공한 경우
                            if (response.body() == CommentPostStatus("success")) {
                                // 댓글 리스트 초기화 하기
                                resultList.clear()

                                // 댓글 리스트 받아오기
                                renewReply()
                                binding.dpReplyEdt.setText("")
                            } else {
                                printErrorMsg()
                            }
                        }

                        override fun onFailure(call: Call<CommentPostStatus>, t: Throwable) {
                            printErrorMsg()
                        }

                    })
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        renewReply()
    }

    fun printErrorMsg(){
        var dialog = AlertDialog.Builder(this@DetailPostActivity)
        dialog.setTitle("오류")
        dialog.setMessage("")
        dialog.show()
    }

    fun renewReply(){
        resultList.clear()
        // 댓글 불러오기
        retrofitService.requestReplyList(postId).enqueue(object : Callback<Reply>{
            override fun onResponse(call: Call<Reply>, response: Response<Reply>) {
                response.body()?.let { resultList.addAll(it) }
                recyclerAdapterDP.submitList(resultList.toList())
            }

            override fun onFailure(call: Call<Reply>, t: Throwable) {
                Log.d("empty", "Empty")
            }

        })
    }

}