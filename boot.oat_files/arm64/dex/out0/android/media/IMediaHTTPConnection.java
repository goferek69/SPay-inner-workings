package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMediaHTTPConnection extends IInterface {

    public static abstract class Stub extends Binder implements IMediaHTTPConnection {
        private static final String DESCRIPTOR = "android.media.IMediaHTTPConnection";
        static final int TRANSACTION_checkFlag = 10;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_getMIMEType = 5;
        static final int TRANSACTION_getProperties = 7;
        static final int TRANSACTION_getSize = 4;
        static final int TRANSACTION_getUri = 6;
        static final int TRANSACTION_readAt = 3;
        static final int TRANSACTION_setProxy = 9;
        static final int TRANSACTION_setReadTimeOut = 8;

        private static class Proxy implements IMediaHTTPConnection {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public IBinder connect(String uri, String headers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uri);
                    _data.writeString(headers);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int readAt(long offset, int size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(offset);
                    _data.writeInt(size);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMIMEType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getUri() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getProperties(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setReadTimeOut(int timeoutMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeoutMs);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setProxy(String proxy_ip, int proxy_port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(proxy_ip);
                    _data.writeInt(proxy_port);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long checkFlag() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMediaHTTPConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMediaHTTPConnection)) {
                return new Proxy(obj);
            }
            return (IMediaHTTPConnection) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            long _result;
            String _result2;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _result3 = connect(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result3);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    disconnect();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = readAt(data.readLong(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getSize();
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getMIMEType();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getUri();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getProperties(data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setReadTimeOut(data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setProxy(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result = checkFlag();
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    long checkFlag() throws RemoteException;

    IBinder connect(String str, String str2) throws RemoteException;

    void disconnect() throws RemoteException;

    String getMIMEType() throws RemoteException;

    long getProperties(int i) throws RemoteException;

    long getSize() throws RemoteException;

    String getUri() throws RemoteException;

    int readAt(long j, int i) throws RemoteException;

    void setProxy(String str, int i) throws RemoteException;

    void setReadTimeOut(int i) throws RemoteException;
}
