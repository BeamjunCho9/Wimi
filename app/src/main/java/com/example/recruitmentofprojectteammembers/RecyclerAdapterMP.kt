package com.example.recruitmentofprojectteammembers

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recruitmentofprojectteammembers.databinding.PostTitleBinding
import data.PostModel
import data.PostModelItem

class RecyclerAdapterMP : ListAdapter<PostModelItem, RecyclerAdapterMP.ViewHolder>(diffUtil){

    inner class ViewHolder(var binding: PostTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(item: PostModelItem) {
            binding.apply {
                postTitle.text = item.title
                postCreateMem.text = "개발자 ${item.create_member_id}"
            }
            // 리사이클러뷰 아이템 클릭 이벤트 설정
            itemView.setOnClickListener(){
                val intent = Intent(context, DetailMyPostActivity::class.java)
                intent.putExtra("title", item.title)
                intent.putExtra("post_id", item.post_id)
                intent.putExtra("create_member_id", item.create_member_id)
                intent.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // currentList: 해당 Adapter에 "submitList()"를 통해 삽입한 아이템 리스트
        holder.bind(currentList[position])
    }

    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<PostModelItem>() {

            //            두 아이템이 동일한 아이템인지 확인
            override fun areItemsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
                return oldItem == newItem
            }

            //            두 아이템이 동일한 내용을 가지고 있는지 확인
            override fun areContentsTheSame(oldItem: PostModelItem, newItem: PostModelItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}