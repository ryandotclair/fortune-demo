package io.pivotal.pcf.demo.fortunebackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by azwickey on 1/12/18.
 */
@Controller
public class BackendController {

    private static Logger LOG = LoggerFactory.getLogger(BackendController.class);

    @Autowired
    private FortuneRepository _repository;

    @RequestMapping(value = "/fortune/{id}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public Fortune get(@PathVariable Long id) {
        LOG.info("Getting fortune for key [" + id + "] from Redis");
        Optional<Fortune> f = _repository.findById(id);
        LOG.debug("Fortune-[" + f + "]");
        return f.get();
    }

    @RequestMapping(value = "/fortune", method = RequestMethod.PUT, consumes="application/json")
    @ResponseBody
    @CrossOrigin
    public Fortune save(@RequestBody Fortune input) {
        LOG.info("Saving fortune [" + input + "] into Redis");
        Fortune f =_repository.save(input);
        LOG.debug("Fortune-[" + f + "]");
        return f;
    }

    @RequestMapping(value = "/fortune", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public Fortune random() {
        LOG.info("Getting random fortune from Redis");
        List<Fortune> all = all();

        if(all.isEmpty()) return null;

        Fortune f = all.get(new Random().nextInt(all.size()));
        LOG.debug("Fortune-[" + f + "]");
        return f;
    }

    @RequestMapping(value = "/fortunes", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public List<Fortune> all() {
        LOG.info("Getting all fortunes from Redis");

        List<Fortune> fortunes = new ArrayList<>();
        _repository.findAll().forEach(fortune -> fortunes.add(fortune));
        // cpu load for 20 minutes
        // ********* THIS PART WAS ADDED BY HOWARD **********
        // load(1, 20, 300000);
        // **************************************************
        LOG.info("Retrieved " + fortunes.size() + " fortunes");
        return Collections.unmodifiableList(fortunes);
    }

    @RequestMapping(value = "/clear", method = RequestMethod.DELETE)
    @ResponseBody
    @CrossOrigin
    public List<Fortune> clear() {
        LOG.info("Clearing all fortunes from Redis");
        _repository.deleteAll();

        LOG.info("Deleted");
        return Collections.EMPTY_LIST;
    }

    @RequestMapping(value = "/kill", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public void kill() {
        System.exit(-1);
    }
    
    /**
     * loadgen codes
     * duration is in milliseconds (so, 30,000 is 30 sec)
     */
    private void load(int core, int threadsPerCore, long duration) {
        int numCore = core;
        int numThreadsPerCore = threadsPerCore;
        double load = 0.9;
        for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
            new BusyThread("Thread" + thread, load, duration).start();
        }
    }
    
    /**
     * Thread that actually generates the given load
     * @author Sriram
     */
    private static class BusyThread extends Thread {
        private double load;
        private long duration;

        /**
         * Constructor which creates the thread
         * @param name Name of this thread
         * @param load Load % that this thread should generate
         * @param duration Duration that this thread should generate the load for
         */
        public BusyThread(String name, double load, long duration) {
            super(name);
            this.load = load;
            this.duration = duration;
        }

        /**
         * Generates the load when run
         */
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try {
                // Loop for the given duration
                while (System.currentTimeMillis() - startTime < duration) {
                    // Every 100ms, sleep for the percentage of unladen time
                    if (System.currentTimeMillis() % 100 == 0) {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
