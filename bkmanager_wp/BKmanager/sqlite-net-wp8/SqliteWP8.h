x���Mo�@��J�5�b�N�J>`��_�^,�|G��&���^z@������;�$�H��0Et�<!0�~>�Q����l�cX��@i��v�+ӿ����&[�=��������*����Y4���M�K��4��y���Im�SD����I��X p�kj����x��7i���N��j�����8�I1
Lj�XUx�`ΔXcTn9�|��� %w�O2#h���,�[��
�t��Ћ7�'x9�U2�|����G�b8�V=lNJ�-���Z�V�A�̱jq��'ً�-o���!)�I��$�Q���G��|�0
��
�_V*�rb�׿�uƪ��&w���-�{'1ׂ�4�A����|����F���E��π�QD��Z<��&`w7�Q���a��N�h��ݎ�HW����㣼�7n��f'�8N(Eo���;d1�=e��Y����%J���W9|�Ű���:���4i����V���ٛ�*�/��e~'�q�r�P�y�_��3�#��odG�                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        E OR OTHER DEALINGS IN THE SOFTWARE.
*/

#pragma once

namespace Sqlite
{
    /*
    Utility class for wrapping sqlite3 "handles".
    */
    public ref class Database sealed
    {
    internal:
    internal:
        Database(sqlite3* db) : _handle(db)
        {
        }

        property sqlite3* Handle
        { 
            sqlite3* get()
            {
                return _handle;
            }
        }

    private:
        sqlite3* _handle;
    };

    /*
    Utility class for wrapping sqlite3_stmt "handles".
    */
    public ref class Statement sealed
    {
    internal:
        Statement(sqlite3_stmt* statement) : _handle(statement)
        {
        }

        property sqlite3_stmt* Handle
        { 
            sqlite3_stmt* get()
            {
                return _handle;
            }
        }

    private:
        sqlite3_stmt* _handle;
    };

    /*
    This class is simply a C++/CX wrapper around sqlite3 exports that sqlite.net depends on.
    Consult the sqlite documentation on what they do.
    */
    public ref class Sqlite3 sealed
    {
    public:
        static int sqlite3_open(Platform::String^ filename, Database^* db);
        static int sqlite3_open_v2(Platform::String^ filename, Database^* db, int flags, Platform::String^ zVfs);
        static int sqlite3_close(Database^ db);
        static int sqlite3_busy_timeout(Database^ db, int miliseconds);
        static int sqlite3_changes(Database^ db);
        static int sqlite3_prepare_v2(Database^ db, Platform::String^ query, Statement^* statement);
        static int sqlite3_step(Statement^ statement);
        static int sqlite3_reset(Statement^ statement);
        static int sqlite3_finalize(Statement^ statement);
        static int64 sqlite3_last_insert_rowid(Database^ db);
        static Platform::String^ sqlite3_errmsg(Database^ db);
        static int sqlite3_bind_parameter_index(Statement^ statement, Platform::String^ name);
        static int sqlite3_bind_null(Statement^ statement, int index);
        static int sqlite3_bind_int(Statement^ statement, int index, int value);
        static int sqlite3_bind_int64(Statement^ statement, int index, int64 value);
        static int sqlite3_bind_double(Statement^ statement, int index, double value);
        static int sqlite3_bind_text(Statement^ statement, int index, Platform::String^ value, int length);
        static int sqlite3_bind_blob(Statement^ statement, int index, const Platform::Array<uint8>^ value, int length);	
        static int sqlite3_column_count(Statement^rstatement);
        static Platform::String^ sqlite3_column_name(Statement^ statement, int index);
        static int sqlite3_column_type(Statement^ statement, int index);
        static int sqlite3_column_int(Statement^ statement, int index);
        static int64 sqlite3_column_int64(Statement^ statement, int index);
        static double sqlite3_column_double(Statement^ statement, int index);
        static Platform::String^ sqlite3_column_text(Statement^ statement, int index);
        static Platform::Array<uint8>^x�Ȼ�0 @�_1]]���&�L�UR�,H�Bx����s' �Z%uk���bYQ���B��?�5� �p�����^�%��G#e�u�ga�y<<[�Ťl[��h�Rݮ�5�#g[�ti�"m�H��~T.$��N���2����y� <_6H                                          