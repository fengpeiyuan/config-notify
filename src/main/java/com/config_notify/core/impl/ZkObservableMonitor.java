package com.config_notify.core.impl;


import java.io.IOException;
import java.util.Arrays;

import com.config_notify.core.ConfigChangeObserver;
import com.config_notify.core.ObservableMonitor;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

public class ZkObservableMonitor extends ObservableMonitor implements Watcher, StatCallback {
    final static Logger logger = Logger.getLogger(ZkObservableMonitor.class);
    private ZooKeeper zk;
    private byte prevData[];
    private String znode;
    private String hostport;


    public ZkObservableMonitor(String hostport, String znode,ConfigChangeObserver configChangeObserver) throws IOException {
        super(configChangeObserver);
        this.hostport = hostport;
        this.znode = znode;
        this.zk = new ZooKeeper(this.hostport,60000,this);
        zk.exists(znode, true, this, null);
    }



    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            // We are are being told that the state of the
            // connection has changed
            switch (event.getState()) {
                case SyncConnected:
                    // In this particular example we don't need to do anything
                    // here - watches are automatically re-registered with
                    // server and any watches triggered while the client was
                    // disconnected will be delivered (in order of course)
                    break;
                case Expired:
                    restartZk();
                    break;
            }
        } else {
            if (path != null && path.equals(znode)) {
                // Something has changed on the node, let's find out
                zk.exists(znode, true, this, null);

            }
        }

    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (rc) {
            case Code.Ok:
                exists = true;
                break;
            case Code.NoNode:
                exists = false;
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                restartZk();
                return;
            default:
                // Retry errors
                zk.exists(znode, true, this, null);
                return;
        }

        byte b[] = null;
        if (exists) {
            try {
                b = zk.getData(znode, false, null);
            } catch (KeeperException e) {
                // We don't need to worry about recovering now. The watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if ((b == null && b != prevData)
                || (b != null && !Arrays.equals(prevData, b))) {
            logger.equals("~~~~~~~~"+b.toString());
            this.getData().put("confStr",new String(b));
            setChanged();
            notifyObservers();
            prevData = b;
        }
    }

    private void restartZk() {
        logger.error("$$$$$ restart zookeeper now $$$$$");
        int retry=5;
        boolean start=false;
        do {
            try {
                this.zk = new ZooKeeper(this.hostport, 60000, this);
                zk.exists(znode, true, this, null);
                start=true;
                return;
            } catch (Exception e) {
                logger.error("$$$$$ restart zookeeper now retry:"+retry);
                e.printStackTrace();
            }finally {
                retry--;
            }
        }while (retry>0&&!start);

        logger.error("$$$$$ restart zookeeper last retry failed $$$$$");
    }
}
