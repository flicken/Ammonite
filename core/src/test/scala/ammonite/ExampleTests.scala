package ammonite

import utest._

import scala.collection.mutable

object ExampleTests extends TestSuite{

  val tests = TestSuite { 
    var wd = processWorkingDir
    'cd{
      // cd /usr/local/bin/
      wd /= 'user/'local/'bin

      assert(wd - processWorkingDir == 'user/'local/'bin)
    }
    'findWc{
      // find . -name '*.txt' | xargs wc -l
      wd /= 'src/'test/'resources/'testdata

      val lines = ls.rec(wd) |? (_.ext == "txt") | read.lines | (_.length) sum
      
      assert(lines == 14)
    }
    'addUpScalaSize{
      ls.rec! processWorkingDir |? (_.ext == "scala") | (_.size) |& (_ + _)
    }
    'concatAll{
      ls.rec! wd |? (_.ext == "scala") | read |> write! wd/'target/'test/"omg.txt"
    }

    'rename{
//      val d1/"omg"/x1 = wd
//      val d2/"omg"/x2 = wd
//      ls! wd |? (_.ext == "scala") | (x => mv! x ! x.pref)
    }
    'allSubpathsResolveCorrectly{
      for(abs <- ls.rec! wd){
        val rel = abs - wd
        assert(rel.ups == 0)
        assert(wd / rel == abs)
      }
    }
  }
}
