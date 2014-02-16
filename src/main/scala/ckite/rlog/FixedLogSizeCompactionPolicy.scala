package ckite.rlog
import ckite.RLog
import ckite.util.Logging
import scala.collection.JavaConverters._
import org.mapdb.DB
import java.util.concurrent.atomic.AtomicBoolean

class FixedLogSizeCompactionPolicy(logSize: Long, db: DB) extends CompactionPolicy with Logging {

  val logCompactor = new LogCompactor()
  val compacting = new AtomicBoolean(false)
  
  def apply(rlog: RLog) = {
    if (rlog.size >= logSize) {
      val wasCompacting = compacting.getAndSet(true)
      if (!wasCompacting) {
    	  LOG.debug(s"Log compaction is required")
    	  logCompactor.compact(rlog, db)
          compacting.set(false)
      }
    }
  }

}