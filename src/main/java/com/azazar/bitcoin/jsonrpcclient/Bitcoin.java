/*
 * Bitcoin-JSON-RPC-Client License
 * 
 * Copyright (c) 2013, Mikhail Yevchenko.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the 
 * Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.azazar.bitcoin.jsonrpcclient;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mikhail Yevchenko <m.ṥῥẚɱ.ѓѐḿởύḙ@azazar.com>
 * @author BitBandi
 */
public interface Bitcoin {
    
    //addmultisigaddress
    
    public static enum AddNodeAction {
        ADD,
        REMOVE,
        ONETRY
    }
    public void addNode(String nodeAddress, AddNodeAction action) throws BitcoinException;
    
    //backupwallet
    
    public static interface TxInput {
        public String txid();
        public int vout();
    }

    public static class BasicTxInput implements TxInput{
        public String txid;
        public int vout;

        public BasicTxInput(String txid, int vout) {
            this.txid = txid;
            this.vout = vout;
        }

        public String txid() {
            return txid;
        }

        public int vout() {
            return vout;
        }

    }

    public static interface TxOutput {
        public String address();
        public double amount();
    }

    public static class BasicTxOutput implements TxOutput{
        public String address;
        public double amount;

        public BasicTxOutput(String address, double amount) {
            this.address = address;
            this.amount = amount;
        }

        public String address() {
            return address;
        }

        public double amount() {
            return amount;
        }
    }

    /**
     * Use BitcoinRawTxBuilder , which is more convenient
     * @param inputs
     * @param outputs
     * @return
     * @throws BitcoinException 
     */
    public String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs) throws BitcoinException;
    
    public RawTransaction decodeRawTransaction(String hex) throws BitcoinException;
    
    public String dumpPrivKey(String address) throws BitcoinException;
    
    //encryptwallet
    
    public String getAccount(String address) throws BitcoinException;

    public String getAccountAddress(String account) throws BitcoinException;

    //getaddednodeinfo
    
    public List<String> getAddressesByAccount(String account) throws BitcoinException;
    
    /**
     * 
     * @return returns the server's total available balance
     * @throws BitcoinException 
     */
    public double getBalance() throws BitcoinException;
    
    /**
     * 
     * @param account
     * @return returns the balance in the account
     * @throws BitcoinException 
     */
    public double getBalance(String account) throws BitcoinException;
    
    /**
     * 
     * @param account
     * @param minConf
     * @return returns the balance in the account
     * @throws BitcoinException 
     */
    public double getBalance(String account, int minConf) throws BitcoinException;

    public static interface Block {
        public String hash();
        public int confirmations();
        public int size();
        public int height();
        public int version();
        public String merkleRoot();
        public List<String> tx();
        public Date time();
        public long nonce();
        public String bits();
        public double difficulty();
        public String previousHash();
        public String nextHash();
        public Block previous() throws BitcoinException;
        public Block next() throws BitcoinException;
    }
    public Block getBlock(String blockHash) throws BitcoinException;
    
    public int getBlockCount() throws BitcoinException;
    
    public String getBlockHash(int blockId) throws BitcoinException;
    
    //getblocknumber - deprecated
    
    public int getConnectionCount() throws BitcoinException;
    
    public double getDifficulty() throws BitcoinException;
    
    public boolean getGenerate() throws BitcoinException;
    
    public double getHashesPerSec() throws BitcoinException;
    
    @Deprecated
    public static interface Info {
        public int version();
        public int protocolVersion();
        public int walletVersion();
        public double balance();
        public int blocks();
        public int timeOffset();
        public int connections();
        public String proxy();
        public double difficulty();
        public boolean testnet();
        public int keyPoolOldest();
        public int keyPoolSize();
        public int unlockedUntil();
        public double payTxFee();
        public double relayFee();
        public String errors();
    }
    
    @Deprecated
    public Info getInfo() throws BitcoinException;
    
    //getmemorypool
    
    public static interface MiningInfo {
        public int blocks();
        public int currentBlockSize();
        public int currentBlockTx();
        public double difficulty();
        public String errors();
        public int genProcLimit();
        public double networkHashPs();
        public int pooledTx();
        public boolean testnet();
        public String chain();
        public boolean generate();
    }
    public MiningInfo getMiningInfo() throws BitcoinException;
    
    //getnewaddress
    public String getNewAddress() throws BitcoinException;
    public String getNewAddress(String account) throws BitcoinException;
    
    public static interface PeerInfo {
        public String addr();
        public String services();
        public int lastSend();
        public int lastRecv();
        public int bytesSent();
        public int bytesRecv();
        public int blocksRequested();
        public Date connTime();
        public int version();
        public String subver();
        public boolean inbound();
        public int startingHeight();
        public int banScore();
    }
    public PeerInfo getPeerInfo() throws BitcoinException;
    
    //getrawmempool
    
    public String getRawTransactionHex(String txId) throws BitcoinException;
    
    public interface RawTransaction {
        public String hex();
        public String txId();
        public int version();
        public long lockTime();
        /*
         * 
         */
        public interface In extends TxInput {
            public Map<String, Object> scriptSig();
            public long sequence();
            
            public RawTransaction getTransaction();
            public Out getTransactionOutput();
        }
        
        /**
         * This method should be replaced someday
         */
        public List<In> vIn(); // TODO : Create special interface instead of this
        
        public interface Out {
            public double value();
            public int n();
            
            public interface ScriptPubKey {
                public String asm();
                public String hex();
                public int reqSigs();
                public String type();
                public List<String> addresses();
            }
            
            public ScriptPubKey scriptPubKey();
            
            public TxInput toInput();
            
            public RawTransaction transaction();

        }
        
        /**
         * This method should be replaced someday
         */
        public List<Out> vOut(); // TODO : Create special interface instead of this
        public String blockHash();
        public int confirmations();
        public Date time();
        public Date blocktime();
    }
    
    public RawTransaction getRawTransaction(String txId) throws BitcoinException;
    
    public double getReceivedByAccount(String account) throws BitcoinException;
    /**
     * Returns the total amount received by &lt;account&gt; in transactions with at least [minconf] confirmations. While some might
     * consider this obvious, value reported by this only considers *receiving* transactions. It does not check payments that have been made
     * *from* this account. In other words, this is not "getaddressbalance". Works only for addresses in the local wallet, external
     * addresses will always show 0.
     *
     * @param account
     * @param minConf
     * @return the total amount received by &lt;account&gt;
     */
    public double getReceivedByAccount(String account, int minConf) throws BitcoinException;
    
    public double getReceivedByAddress(String address) throws BitcoinException;
    /**
     * Returns the total amount received by &lt;bitcoinaddress&gt; in transactions with at least [minconf] confirmations. While some might
     * consider this obvious, value reported by this only considers *receiving* transactions. It does not check payments that have been made
     * *from* this address. In other words, this is not "getaddressbalance". Works only for addresses in the local wallet, external
     * addresses will always show 0.
     *
     * @param address
     * @param minConf
     * @return the total amount received by &lt;bitcoinaddress&gt;
     */
    public double getReceivedByAddress(String address, int minConf) throws BitcoinException;
    
    public RawTransaction getTransaction(String txId) throws BitcoinException;
    
    public static interface TxOutSetInfo {
        public int height();
        public String bestBlock();
        public int transactions();
        public int txOuts();
        public int bytesSerialized();
        public String hashSerialized();
        public double totalAmount();
    }
    public TxOutSetInfo getTxOutSetInfo() throws BitcoinException;
    
    public static interface Work {
        public String midstate();
        public String data();
        public String hash1();
        public String target();
    }
    public Work getWork() throws BitcoinException;
    
    //help
    
    public void importPrivKey(String bitcoinPrivKey) throws BitcoinException;
    public void importPrivKey(String bitcoinPrivKey, String label) throws BitcoinException;
    public void importPrivKey(String bitcoinPrivKey, String label, boolean rescan) throws BitcoinException;
    
    //keypoolrefill
    
    /**
     * listaccounts [minconf=1]
     * 
     * @return Map that has account names as keys, account balances as values
     * @throws BitcoinException 
     */
    public Map<String, Number> listAccounts() throws BitcoinException;
    public Map<String, Number> listAccounts(int minConf) throws BitcoinException;
    
    public static interface ReceiveAddress {
        
        public String address();
        public double balance();
        public String account();

    }

    public List<List<ReceiveAddress>> listAddressGroupings() throws BitcoinException;
    
    public List<ReceivedAddress> listReceivedByAccount() throws BitcoinException;
    public List<ReceivedAddress> listReceivedByAccount(int minConf) throws BitcoinException;
    public List<ReceivedAddress> listReceivedByAccount(int minConf, boolean includeEmpty) throws BitcoinException;
    
    public static interface ReceivedAddress {
        public String address();
        public String account();
        public double amount();
        public int confirmations();
    }

    public List<ReceivedAddress> listReceivedByAddress() throws BitcoinException;
    public List<ReceivedAddress> listReceivedByAddress(int minConf) throws BitcoinException;
    public List<ReceivedAddress> listReceivedByAddress(int minConf, boolean includeEmpty) throws BitcoinException;
    
    /**
     * returned by listsinceblock and  listtransactions
     */
    public static interface Transaction {
        public String account();
        public String address();
        public String category();
        public double amount();
        public double fee();
        public int confirmations();
        public String blockHash();
        public int blockIndex();
        public Date blockTime();
        public String txId();
        public Date time();
        public Date timeReceived();
        public String comment();
        public String commentTo();
        
        public RawTransaction raw();
    }

    //listsinceblock
    public static interface TransactionsSinceBlock {
        public List<Transaction> transactions();
        public String lastBlock();
    }
    
    public TransactionsSinceBlock listSinceBlock() throws BitcoinException;
    public TransactionsSinceBlock listSinceBlock(String blockHash) throws BitcoinException;
    public TransactionsSinceBlock listSinceBlock(String blockHash, int targetConfirmations) throws BitcoinException;
    
    //listtransactions
    public List<Transaction> listTransactions() throws BitcoinException;
    public List<Transaction> listTransactions(String account) throws BitcoinException;
    public List<Transaction> listTransactions(String account, int count) throws BitcoinException;
    public List<Transaction> listTransactions(String account, int count, int from) throws BitcoinException;
    
    public interface Unspent extends TxInput, TxOutput {
        public String txid();
        public int vout();
        public String address();
        public String account();
        public String scriptPubKey();
        public double amount();
        public int confirmations();
    }

    public List<Unspent> listUnspent() throws BitcoinException;
    public List<Unspent> listUnspent(int minConf) throws BitcoinException;
    public List<Unspent> listUnspent(int minConf, int maxConf) throws BitcoinException;
    public List<Unspent> listUnspent(int minConf, int maxConf, String... addresses) throws BitcoinException;
    
    //listlockunspent
    
    //lockunspent
    
    //move
    
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinException;
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinException;
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinException;
    /**
     * Will send the given amount to the given address, ensuring the account has a valid balance using minConf confirmations.
     * @param fromAccount
     * @param toBitcoinAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param minConf
     * @return the transaction ID if successful
     * @throws BitcoinException 
     */
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment, String commentTo) throws BitcoinException;
    
    public String sendMany(String fromAccount, List<TxOutput> outputs) throws BitcoinException;
    public String sendMany(String fromAccount, List<TxOutput> outputs, int minConf) throws BitcoinException;
    public String sendMany(String fromAccount, List<TxOutput> outputs, int minConf, String comment) throws BitcoinException;
    
    public String sendRawTransaction(String hex) throws BitcoinException;
    
    public String sendToAddress(String toAddress, double amount) throws BitcoinException;
    public String sendToAddress(String toAddress, double amount, String comment) throws BitcoinException;
    /**
     * 
     * @param toAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param comment
     * @param commentTo
     * @return the transaction ID &lt;txid&gt; if successful
     * @throws BitcoinException 
     */
    public String sendToAddress(String toAddress, double amount, String comment, String commentTo) throws BitcoinException;

    //setaccount
    
    //setgenerate
    
    public String signMessage(String address, String message) throws BitcoinException;
    
    public String signRawTransaction(String hex) throws BitcoinException;
    
    //settxfee
    
    public void stop() throws BitcoinException;
    
    public static interface AddressValidationResult {
        public boolean isValid();
        public String address();
        public boolean isMine();
        public boolean isScript();
        public String pubKey();
        public boolean isCompressed();
        public String account();
    }

    public AddressValidationResult validateAddress(String address) throws BitcoinException;
    
    public boolean verifyMessage(String address, String signature, String message) throws BitcoinException;
    
    //walletlock
    
    //walletpassphrase
    
    //walletpassphrasechange
    

}
