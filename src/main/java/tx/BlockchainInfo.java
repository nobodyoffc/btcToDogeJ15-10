package tx;

import java.util.List;

public class BlockchainInfo {
    private String chain;
    private int blocks;
    private int headers;
    private String bestblockhash;
    private double difficulty;
    private int mediantime;
    private double verificationprogress;
    private boolean initialblockdownload;
    private String chainwork;
    private long size_on_disk;
    private boolean pruned;
    private List<Softfork> softforks;
    private Bip9Softfork bip9_softforks;
    private String warnings;

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getHeaders() {
        return headers;
    }

    public void setHeaders(int headers) {
        this.headers = headers;
    }

    public String getBestblockhash() {
        return bestblockhash;
    }

    public void setBestblockhash(String bestblockhash) {
        this.bestblockhash = bestblockhash;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public int getMediantime() {
        return mediantime;
    }

    public void setMediantime(int mediantime) {
        this.mediantime = mediantime;
    }

    public double getVerificationprogress() {
        return verificationprogress;
    }

    public void setVerificationprogress(double verificationprogress) {
        this.verificationprogress = verificationprogress;
    }

    public boolean isInitialblockdownload() {
        return initialblockdownload;
    }

    public void setInitialblockdownload(boolean initialblockdownload) {
        this.initialblockdownload = initialblockdownload;
    }

    public String getChainwork() {
        return chainwork;
    }

    public void setChainwork(String chainwork) {
        this.chainwork = chainwork;
    }

    public long getSize_on_disk() {
        return size_on_disk;
    }

    public void setSize_on_disk(long size_on_disk) {
        this.size_on_disk = size_on_disk;
    }

    public boolean isPruned() {
        return pruned;
    }

    public void setPruned(boolean pruned) {
        this.pruned = pruned;
    }

    public List<Softfork> getSoftforks() {
        return softforks;
    }

    public void setSoftforks(List<Softfork> softforks) {
        this.softforks = softforks;
    }

    public Bip9Softfork getBip9_softforks() {
        return bip9_softforks;
    }

    public void setBip9_softforks(Bip9Softfork bip9_softforks) {
        this.bip9_softforks = bip9_softforks;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public static class Softfork {
        private String id;
        private int version;
        private Reject reject;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public Reject getReject() {
            return reject;
        }

        public void setReject(Reject reject) {
            this.reject = reject;
        }
    }

    public static class Reject {
        private boolean status;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    public static class Bip9Softfork {
        private Csv csv;

        public Csv getCsv() {
            return csv;
        }

        public void setCsv(Csv csv) {
            this.csv = csv;
        }
    }

    public static class Csv {
        private String status;
        private long startTime;
        private long timeout;
        private int since;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        public int getSince() {
            return since;
        }

        public void setSince(int since) {
            this.since = since;
        }
    }
}

