package com.home.gifttest

data class GameRoomItem(val gameMode:Int=0
                        ,val roomName:String="遊戲已結束"
                        ,val country:Int=0
                        ,val limitMode:Int=0
                        ,val userID:String=""
                        ,val roomNumber:Int=0
                        ,val iconUri:String=""
                        ,val documentPath:String=""
                        ,val password:String=""
                        ,val count:Int=0
                        ,var documentID:String=""  //拉下來填充點選的ID
                        ,val applyCount:Int=0)
                        //,val accepted:Int=0) //0為審核中，審核成功經由functions更改為1