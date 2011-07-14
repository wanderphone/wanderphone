<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<?php
header("Content-type:text/html;charset=utf-8");
/*connect to database*/
$con = mysql_connect("localhost","android_mineswip","!lang520BbMm!");
/*connect failed*/
if(!$con)
{
  die('Could not connect:'.mysql_error());
}
/*select the database*/
else
{
  mysql_select_db("android_misehero", $con);
  mysql_query("set names utf8;");
}

/*splash page's application*/
if($_GET[which_use] == 1)
{
  $result = mysql_query("SELECT * FROM wp_user where phone_id='$_GET[phone_id]'");
  /*the result is not null*/
  if(!mysql_num_rows($result))
  {
    echo "<IsRegister>no</IsRegister>";
  }
  else
  {
    echo "<First>\n";
    echo "<IsRegister>yes</IsRegister>\n";
    mysql_select_db("minesweep", $con);
    $result_easy_rank = mysql_query("SELECT * from wp_rank_easy where phone_id='$_GET[phone_id]'");
    $result_normal_rank = mysql_query("SELECT * from wp_rank_normal where phone_id='$_GET[phone_id]'");
    $result_hard_rank = mysql_query("SELECT * from wp_rank_hard where phone_id='$_GET[phone_id]'");
    if(!mysql_num_rows($result_easy_rank))
    {
      echo "<EasyTime>-1</EasyTime>\n";
      echo "<EasyRank>-1</EasyRank>\n";
    }
    else
    {
      while($row_easy = mysql_fetch_array($result_easy_rank))
      {
        echo "<EasyTime>".$row_easy['time']."</EasyTime>\n";
        echo "<EasyRank>".$row_easy['ranking']."</EasyRank>\n";
      }
    }

    if(!mysql_num_rows($result_normal_rank))
    {
      echo "<NormalTime>-1</NormalTime>\n";
      echo "<NormalRank>-1</NormalRank>\n";
    }
    else
    {
      while($row_easy = mysql_fetch_array($result_normal_rank))
      {
        echo "<NormalTime>".$row_easy['time']."</NormalTime>\n";
        echo "<NormalRank>".$row_easy['ranking']."</NormalRank>\n";
      }
    }

    if(!mysql_num_rows($result_hard_rank))
    {
      echo "<HardTime>-1</HardTime>\n";
      echo "<HardRank>-1</HardRank>\n";
    }
    else
    {
      while($row_easy = mysql_fetch_array($result_hard_rank))
      {
        echo "<HardTime>".$row_easy['time']."</HardTime>\n";
        echo "<HardRank>".$row_easy['ranking']."</HardRank>\n";
      }
    }
    echo "</First>\n";
  }
}

/*user register*/
if($_GET[which_use] == 2)
{
  $phone_id = $_GET[phone_id];
  $username = $_GET[username];
  $insert_is_success = mysql_query("insert into wp_user(phone_id,username) values('$phone_id','$username')");
  /*regist success*/
  if($insert_is_success)
  {
    echo "<IsSuccess>yes</IsSuccess>";
    echo $username;
  }
  /*regist failed*/
  else
  {
    echo "<IsSuccess>no</IsSuccess>";
    echo $username;
  }
}

/*user submit the score*/
if($_GET[which_use]==3)
{
  $time_before = -1;
  $rank_before = -1;
  $time_before_send = -1;
  $rank_before_send = -1;
  $rank_now = -1;
  $phone_id = $_GET[phone_id];
  $time = $_GET[time];
  $level = $_GET[level];
  /*easy level*/
  if($level == "easy")
  {
    /*save the last score of user*/
    $result_of_easy = mysql_query("select * from wp_rank_easy where phone_id='$phone_id'");

    if(mysql_num_rows($result_of_easy)==0)
    {
      $time_before = -1;
      $rank_before = -1;
    }
    else
    {
      while($last_score_easy = mysql_fetch_array($result_of_easy))
      {
        $time_before = $last_score_easy['time'];
        $rank_before = $last_score_easy['ranking'];
        $time_before_send = $last_score_easy['time'];
        $rank_before_send = $last_score_easy['ranking'];
      }
    }
    /*if the rank_before is -1,means that there is no record of the user,
    so we need insert into the table*/
    if($rank_before == -1)
    {
      $score_of_all_user = mysql_query("select * from wp_rank_easy order by ranking desc");
      /*score_of_all_user is false,means there are not record in the rank_easy table,
      so the user must be the first one,just need to insert*/
      if(mysql_num_rows($score_of_all_user)==0)
      {
      	$rank_now = 1;
        $rank_this_time = $rank_now;
      	mysql_query("insert into wp_rank_easy(phone_id,time,ranking) values('$phone_id','$time','$rank_now')");
      }
      /**/
      else
      {
        $flag_rank = -1;
        /*select how much record in the table*/
        $count_of_user = mysql_query("select count(*) from wp_rank_easy");
        while($result_of_count = mysql_fetch_array($count_of_user))
        {
          $count_user = $result_of_count['count(*)'];
        }
        while($all_user_score = mysql_fetch_array($score_of_all_user))
        {
          if($time < $all_user_score['time'])
          {
            $rank_now = $all_user_score['ranking'];
            $flag_rank = $rank_now;
            $other_rank = $all_user_score['ranking']+1;
            $other_phone_id = $all_user_score['phone_id'];
            mysql_query("update wp_rank_easy set ranking='$other_rank' where phone_id='$other_phone_id'");
          }
          else if($time == $all_user_score['time'])
          {
            $rank_now = $all_user_score['ranking'];
            break;
          }
          else if($time > $all_user_score['time'])
          {
            if($flag_rank == -1)
            {
              $rank_now = $count_user+1;
            }
            else
            {
              $rank_now = $flag_rank;
            }
            break;
          }
        }
        $rank_this_time = $rank_now;
        mysql_query("insert into wp_rank_easy(phone_id,time,ranking) values('$phone_id','$time','$rank_now')");
      }
    }
    /*the rank_before is not -1,means that there is record of the user,
    we just need to updata the record of the user*/
    else
    {
      $score_of_all_user = mysql_query("select * from wp_rank_easy where time<='$time_before' order by ranking desc");
      if(!$score_of_all_user)
      {
        mysql_query("update wp_rank_easy set time='$time' ranking='1' where phone_id='$phone_id'");
      }
      else
      {
        while($all_user = mysql_fetch_array($score_of_all_user))
        {
          if($time < $all_user['time'])
          {
            $other_phone_id = $all_user['phone_id'];
            mysql_query("update wp_rank_easy set ranking='$rank_before' where phone_id='$other_phone_id'");
            $rank_before = $all_user['ranking'];
          }
          else if($time == $all_user['time'])
          {
            $rank_now = $all_user['ranking'];
            break;
          }
          else if($time > $all_user[time])
          {
             break;
          }
        }
        $rank_this_time = $rank_before;
        if($time < $time_before)
       	 mysql_query("update wp_rank_easy set time='$time',ranking='$rank_before' where phone_id='$phone_id'");
      }
    }
    $result_of_rank_easy = mysql_query("select * from wp_rank_easy where phone_id='$phone_id'");
    while($row_rank_easy = mysql_fetch_array($result_of_rank_easy)){
      echo "<GameOver>\n";
      echo "<Ranking>".$rank_this_time."</Ranking>\n";
      echo "<Time>".$time."</Time>\n";
      echo "<RankingNow>".$row_rank_easy['ranking']."</RankingNow>\n";
      echo "<TimeNow>".$row_rank_easy['time']."</TimeNow>\n";
      echo "</GameOver>";
    }
  }
  /*normal rank*/
  if($level == "normal")
  {
    /*save the last score of user*/
    $result_of_easy = mysql_query("select * from wp_rank_normal where phone_id='$phone_id'");

    if(mysql_num_rows($result_of_easy)==0)
    {
      $time_before = -1;
      $rank_before = -1;
    }
    else
    {
      while($last_score_easy = mysql_fetch_array($result_of_easy))
      {
        $time_before = $last_score_easy['time'];
        $rank_before = $last_score_easy['ranking'];
        $time_before_send = $last_score_easy['time'];
        $rank_before_send = $last_score_easy['ranking'];
      }
    }
    /*if the rank_before is -1,means that there is no record of the user,
    so we need insert into the table*/
    if($rank_before == -1)
    {
      $score_of_all_user = mysql_query("select * from wp_rank_normal order by ranking desc");
      /*score_of_all_user is false,means there are not record in the rank_easy table,
      so the user must be the first one,just need to insert*/
      if(mysql_num_rows($score_of_all_user)==0)
      {
      	$rank_now = 1;
        $rank_this_time = $rank_now;
      	mysql_query("insert into wp_rank_normal(phone_id,time,ranking) values('$phone_id','$time','$rank_now')");
      }
      /**/
      else
      {
        $flag_rank = -1;
        /*select how much record in the table*/
        $count_of_user = mysql_query("select count(*) from wp_rank_normal");
        while($result_of_count = mysql_fetch_array($count_of_user))
        {
          $count_user = $result_of_count['count(*)'];
        }
        while($all_user_score = mysql_fetch_array($score_of_all_user))
        {
          if($time < $all_user_score['time'])
          {
            $rank_now = $all_user_score['ranking'];
            $flag_rank = $rank_now;
            $other_rank = $all_user_score['ranking']+1;
            $other_phone_id = $all_user_score['phone_id'];
            mysql_query("update wp_rank_normal set ranking='$other_rank' where phone_id='$other_phone_id'");
          }
          else if($time == $all_user_score['time'])
          {
            $rank_now = $all_user_score['ranking'];
            break;
          }
          else if($time > $all_user_score['time'])
          {
            if($flag_rank == -1)
            {
              $rank_now = $count_user+1;
            }
            else
            {
              $rank_now = $flag_rank;
            }
            break;
          }
        }
        $rank_this_time = $rank_now;
        mysql_query("insert into wp_rank_normal(phone_id,time,ranking) values('$phone_id','$time','$rank_now')");
      }
    }
    /*the rank_before is not -1,means that there is record of the user,
    we just need to updata the record of the user*/
    else
    {
      $score_of_all_user = mysql_query("select * from wp_rank_normal where time<='$time_before' order by ranking desc");
      if(!$score_of_all_user)
      {
        mysql_query("update wp_rank_normal set time='$time' ranking='1' where phone_id='$phone_id'");
      }
      else
      {
        while($all_user = mysql_fetch_array($score_of_all_user))
        {
          if($time < $all_user['time'])
          {
            $other_phone_id = $all_user['phone_id'];
            mysql_query("update wp_rank_normal set ranking='$rank_before' where phone_id='$other_phone_id'");
            $rank_before = $all_user['ranking'];
          }
          else if($time == $all_user['time'])
          {
            $rank_now = $all_user['ranking'];
            break;
          }
          else if($time > $all_user[time])
          {
             break;
          }
        }
        $rank_this_time = $rank_before;
        if($time < $time_before)
       	 mysql_query("update wp_rank_normal set time='$time',ranking='$rank_before' where phone_id='$phone_id'");
      }
    }
    $result_of_rank_easy = mysql_query("select * from wp_rank_normal where phone_id='$phone_id'");
    while($row_rank_easy = mysql_fetch_array($result_of_rank_easy)){
      echo "<GameOver>\n";
      echo "<Ranking>".$rank_this_time."</Ranking>\n";
      echo "<Time>".$time."</Time>\n";
      echo "<RankingNow>".$row_rank_easy['ranking']."</RankingNow>\n";
      echo "<TimeNow>".$row_rank_easy['time']."</TimeNow>\n";
      echo "</GameOver>";
    }
  }
 /*hard rank*/
  if($level == "hard")
  {
    /*save the last score of user*/
    $result_of_easy = mysql_query("select * from wp_rank_hard where phone_id='$phone_id'");

    if(mysql_num_rows($result_of_easy)==0)
    {
      $time_before = -1;
      $rank_before = -1;
    }
    else
    {
      while($last_score_easy = mysql_fetch_array($result_of_easy))
      {
        $time_before = $last_score_easy['time'];
        $rank_before = $last_score_easy['ranking'];
        $time_before_send = $last_score_easy['time'];
        $rank_before_send = $last_score_easy['ranking'];
      }
    }
    /*if the rank_before is -1,means that there is no record of the user,
    so we need insert into the table*/
    if($rank_before == -1)
    {
      $score_of_all_user = mysql_query("select * from wp_rank_hard order by ranking desc");
      /*score_of_all_user is false,means there are not record in the rank_easy table,
      so the user must be the first one,just need to insert*/
      if(mysql_num_rows($score_of_all_user)==0)
      {
      	$rank_now = 1;
        $rank_this_time = $rank_now;
      	mysql_query("insert into wp_rank_hard(phone_id,time,ranking) values('$phone_id','$time','$rank_now')");
      }
      /**/
      else
      {
        $flag_rank = -1;
        /*select how much record in the table*/
        $count_of_user = mysql_query("select count(*) from rank_hard");
        while($result_of_count = mysql_fetch_array($count_of_user))
        {
          $count_user = $result_of_count['count(*)'];
        }
        while($all_user_score = mysql_fetch_array($score_of_all_user))
        {
          if($time < $all_user_score['time'])
          {
            $rank_now = $all_user_score['ranking'];
            $flag_rank = $rank_now;
            $other_rank = $all_user_score['ranking']+1;
            $other_phone_id = $all_user_score['phone_id'];
            mysql_query("update wp_rank_hard set ranking='$other_rank' where phone_id='$other_phone_id'");
          }
          else if($time == $all_user_score['time'])
          {
            $rank_now = $all_user_score['ranking'];
            break;
          }
          else if($time > $all_user_score['time'])
          {
            if($flag_rank == -1)
            {
              $rank_now = $count_user+1;
            }
            else
            {
              $rank_now = $flag_rank;
            }
            break;
          }
        }
        $rank_this_time = $rank_now;
        mysql_query("insert into wp_rank_hard(phone_id,time,ranking) values('$phone_id','$time','$rank_now')");
      }
    }
    /*the rank_before is not -1,means that there is record of the user,
    we just need to updata the record of the user*/
    else
    {
      $score_of_all_user = mysql_query("select * from wp_rank_hard where time<='$time_before' order by ranking desc");
      if(!$score_of_all_user)
      {
        mysql_query("update wp_rank_hard set time='$time' ranking='1' where phone_id='$phone_id'");
      }
      else
      {
        while($all_user = mysql_fetch_array($score_of_all_user))
        {
          if($time < $all_user['time'])
          {
            $other_phone_id = $all_user['phone_id'];
            mysql_query("update wp_rank_hard set ranking='$rank_before' where phone_id='$other_phone_id'");
            $rank_before = $all_user['ranking'];
          }
          else if($time == $all_user['time'])
          {
            $rank_now = $all_user['ranking'];
            break;
          }
          else if($time > $all_user[time])
          {
             break;
          }
        }
        $rank_this_time = $rank_before;
        if($time < $time_before)
       	 mysql_query("update wp_rank_hard set time='$time',ranking='$rank_before' where phone_id='$phone_id'");
      }
    }
    $result_of_rank_easy = mysql_query("select * from wp_rank_hard where phone_id='$phone_id'");
    while($row_rank_easy = mysql_fetch_array($result_of_rank_easy)){
      echo "<GameOver>\n";
      echo "<Ranking>".$rank_this_time."</Ranking>\n";
      echo "<Time>".$time."</Time>\n";
      echo "<RankingNow>".$row_rank_easy['ranking']."</RankingNow>\n";
      echo "<TimeNow>".$row_rank_easy['time']."</TimeNow>\n";
      echo "</GameOver>";
    }
  }
}
/*client apply all info of rank */
if($_GET[which_use] == 4)
{
  $level = $_GET[level];
  if($level == "easy")
  {
    echo "<RankEasy>\n";
    $result_all_rank_easy = mysql_query("select wp_user.username,wp_rank_easy.time,wp_rank_easy.ranking from
                             wp_user,wp_rank_easy where wp_user.phone_id=wp_rank_easy.phone_id order by wp_rank_easy.ranking");
    while($row_all_rank_easy = mysql_fetch_array($result_all_rank_easy))
    {
      echo "<RankInfo>\n";
      echo "<UserName>".$row_all_rank_easy['username']."</UserName>\n";
      echo "<TimeRank>".$row_all_rank_easy['time']."</TimeRank>\n";
      echo "<Rank>".$row_all_rank_easy['ranking']."</Rank>\n";
      echo "</RankInfo>\n";
    }
    echo "</RankEasy>";
  }

  if($level == "normal")
  {
    echo "<RankNormal>\n";
    $result_all_rank_normal = mysql_query("select wp_user.username,wp_rank_normal.time,wp_rank_normal.ranking from
                             wp_user,wp_rank_normal where wp_user.phone_id=wp_rank_normal.phone_id order by wp_rank_normal.ranking");
    while($row_all_rank_normal = mysql_fetch_array($result_all_rank_normal))
    {
      echo "<RankInfo>\n";
      echo "<UserName>".$row_all_rank_normal['username']."</UserName>\n";
      echo "<TimeRank>".$row_all_rank_normal['time']."</TimeRank>\n";
      echo "<Rank>".$row_all_rank_normal['ranking']."</Rank>\n";
      echo "</RankInfo>\n";
    }
    echo "</RankNormal>";
  }

  if($level == "hard")
  {
    echo "<RankHard>\n";
    $result_all_rank_hard = mysql_query("select wp_user.username,wp_rank_hard.time,wp_rand_hark.ranking from
                             wp_user,wp_rank_hard where wp_user.phone_id=wp_rank_hard.phone_id order by wp_rank_hard.ranking");
    while($row_all_rank_hard = mysql_fetch_array($result_all_rank_hard))
    {
      echo "<RankInfo>\n";
      echo "<UserName>".$row_all_rank_easy['username']."</UserName>\n";
      echo "<TimeRank>".$row_all_rank_easy['time']."</TimeRank>\n";
      echo "<Rank>".$row_all_rank_easy['ranking']."</Rank>\n";
      echo "</RankInfo>\n";
    }
    echo "</RankHard>";
  }
}
?>


